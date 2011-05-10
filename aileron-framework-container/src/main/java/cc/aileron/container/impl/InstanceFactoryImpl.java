/**
 *
 */
package cc.aileron.container.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cc.aileron.container.Inject;
import cc.aileron.container.InjectOption;
import cc.aileron.container.Provider;
import cc.aileron.container.model.Binding;
import cc.aileron.container.model.BindingMap;
import cc.aileron.container.model.InstanceFactory;
import cc.aileron.container.model.InstanceMap;

/**
 * @author aileron
 */
public class InstanceFactoryImpl implements InstanceFactory
{
    @Override
    public <T> T getInstance(final Binding<T> binding)
    {
        if (binding.implementType() == null)
        {
            return binding.provider().get();
        }

        final Constructor<? extends T> constructor = pickupConstractor(binding.implementType());
        final Type[] parameterTypes = constructor.getGenericParameterTypes();
        if (parameterTypes.length == 0)
        {
            try
            {
                return constructor.newInstance();
            }
            catch (final Exception e)
            {
                throw new Error(e);
            }
        }
        final Annotation[][] annotations = constructor.getParameterAnnotations();
        final Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
        {
            parameters[i] = findObject(parameterTypes[i], annotations[i]);
        }
        try
        {
            return constructor.newInstance(parameters);
        }
        catch (final Exception e)
        {
            throw new Error(e);
        }
    }

    @Override
    public <T> T injectMember(final T object)
    {
        for (final Method method : object.getClass().getMethods())
        {
            if (!method.isAnnotationPresent(Inject.class))
            {
                continue;
            }
            final Annotation[][] annotations = method.getParameterAnnotations();
            final Type[] parameterTypes = method.getGenericParameterTypes();
            final Object[] parameter = new Object[parameterTypes.length];
            for (int i = 0, max = parameterTypes.length; i < max; i++)
            {
                parameter[i] = findObject(parameterTypes[i], annotations[i]);
            }
            try
            {
                method.setAccessible(true);
                method.invoke(object, parameter);
            }
            catch (final Exception e)
            {
                throw new Error(e);
            }
        }
        for (final Field field : object.getClass().getFields())
        {
            if (!field.isAnnotationPresent(Inject.class))
            {
                continue;
            }
            final Annotation[] annotations = field.getAnnotations();
            final Type type = field.getGenericType();
            try
            {
                field.setAccessible(true);
                field.set(object, findObject(type, annotations));
            }
            catch (final Exception e)
            {
                throw new Error(e);
            }
        }
        return object;
    }

    private Object findObject(final Type type, final Annotation[] annotations)
    {
        final Annotation option = pickupInjectOption(annotations);
        if (type instanceof ParameterizedType == false)
        {
            final Binding<?> binding = bindingMap.getBinding(type, option);
            return instanceMap.getProvider(binding).get();
        }
        final ParameterizedType ptype = (ParameterizedType) type;
        final Class<?> rType = (Class<?>) ptype.getRawType();
        if (Provider.class.isAssignableFrom(rType))
        {
            final Binding<?> binding = bindingMap.getBinding(ptype.getActualTypeArguments()[0],
                    option);
            return instanceMap.getProvider(binding);
        }
        final Binding<?> binding = bindingMap.getBinding(type, option);
        return instanceMap.getProvider(binding).get();
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> pickupConstractor(final Class<T> target)
    {
        final Constructor<?>[] constructors = target.getConstructors();
        if (constructors.length == 1)
        {
            return (Constructor<T>) constructors[0];
        }
        for (final Constructor<?> constructor : constructors)
        {
            if (constructor.isAnnotationPresent(Inject.class))
            {
                return (Constructor<T>) constructor;
            }
        }
        throw new Error("not found inject annotation");
    }

    private Annotation pickupInjectOption(final Annotation[] annotations)
    {
        for (final Annotation annotation : annotations)
        {
            if (annotation.annotationType()
                    .isAnnotationPresent(InjectOption.class))
            {
                return annotation;
            }
        }
        return null;
    }

    /**
     * @param bindingMap
     * @param instanceMap
     */
    public InstanceFactoryImpl(final BindingMap bindingMap,
            final InstanceMap instanceMap)
    {
        this.bindingMap = bindingMap;
        this.instanceMap = instanceMap;
    }

    private final BindingMap bindingMap;
    private final InstanceMap instanceMap;
}
