/**
 *
 */
package cc.aileron.container.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

import cc.aileron.container.ImplementedBy;
import cc.aileron.container.InjectOption;
import cc.aileron.container.Injector;
import cc.aileron.container.ProvidedBy;
import cc.aileron.container.Provider;
import cc.aileron.container.model.Binding;
import cc.aileron.container.model.BindingAnnotatedMap;
import cc.aileron.container.model.BindingMap;
import cc.aileron.container.model.InstanceMap;
import cc.aileron.container.model.ScopeMap;
import cc.aileron.container.scope.Prototype;
import cc.aileron.container.scope.ScopeAnnotation;
import cc.aileron.container.scope.Singleton;

/**
 * @author aileron
 */
class BindingMapImpl implements BindingMap
{
    @Override
    public <T> Binding<T> bindImplement(final Type type,
            final Class<? extends T> implementType,
            final Annotation annotation, final Class<? extends Annotation> scope)
    {
        return put(type,
                annotation == null ? noneAnnotate : annotation,
                newBinding(annotation, type, implementType, scope));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void bindInstance(final Type type, final T instance,
            final Annotation rawannotation)
    {
        final Annotation annotation = rawannotation == null ? noneAnnotate
                : rawannotation;
        final Class<T> interfaceType = (Class<T>) type;
        put(type, annotation, new Binding<T>()
        {
            @Override
            public Object annotation()
            {
                return annotation;
            }

            @Override
            public Class<? extends T> implementType()
            {
                return null;
            }

            @Override
            public Class<T> interfaceType()
            {
                return interfaceType;
            }

            @Override
            public Provider<T> provider()
            {
                return provider;
            }

            @Override
            public Class<? extends Annotation> scope()
            {
                return Singleton.class;
            }

            private final Provider<T> provider = new Provider<T>()
            {
                @Override
                public T get()
                {
                    return instance;
                }
            };
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Binding<T> bindProviderImplement(final Type type,
            final Class<? extends Provider<T>> to,
            final Annotation rawannotation,
            final Class<? extends Annotation> scope)
    {
        final Annotation annotation = rawannotation == null ? noneAnnotate
                : rawannotation;
        final Class<T> interfaceType = (Class<T>) type;
        return put(type, annotation, new Binding<T>()
        {
            @Override
            public Object annotation()
            {
                return annotation;
            }

            @Override
            public Class<? extends T> implementType()
            {
                return null;
            }

            @Override
            public Class<T> interfaceType()
            {
                return interfaceType;
            }

            @Override
            public Provider<T> provider()
            {
                final Binding<Provider<T>> binding = getBinding(to, annotation);
                return instanceMap.getProvider(binding).get();
            }

            @Override
            public Class<? extends Annotation> scope()
            {
                return scope;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void bindProviderInstance(final Type type,
            final Provider<T> provider, final Annotation rawannotation,
            final Class<? extends Annotation> scope)
    {
        final Annotation annotation = rawannotation == null ? noneAnnotate
                : rawannotation;
        final Class<T> interfaceType = (Class<T>) type;
        put(type, annotation, new Binding<T>()
        {
            @Override
            public Object annotation()
            {
                return annotation;
            }

            @Override
            public Class<? extends T> implementType()
            {
                return null;
            }

            @Override
            public Class<T> interfaceType()
            {
                return interfaceType;
            }

            @Override
            public Provider<T> provider()
            {
                return provider;
            }

            @Override
            public Class<? extends Annotation> scope()
            {
                return scope;
            }
        });
    }

    @Override
    public <T> Binding<T> getBinding(final Type type,
            final Annotation rawannotation)
    {
        final Annotation annotation = rawannotation == null ? noneAnnotate
                : rawannotation;
        final Binding<T> binding = pickupBinding(type, annotation);
        if (binding != null)
        {
            return binding;
        }
        if (((Class<?>) type).isInterface() == false)
        {// 実クラス指定
            return setNoneInterfaceBinding(type, annotation);
        }
        else
        {// binding指定が無い場合はインタフェースのアノテーションによって
            return setInterfaceAnnotateBinding(type, annotation);
        }
    }

    @Override
    public Injector getInjector()
    {
        return injector;
    }

    private BindingAnnotatedMap<Annotation> getBindingAnnotatedMap(
            final Annotation annotation)
    {
        final BindingAnnotatedMap<Annotation> result = map.get(annotation.annotationType());
        if (result != null)
        {
            return result;
        }
        final InjectOption option = annotation.annotationType()
                .getAnnotation(InjectOption.class);
        if (option != null)
        {

        }
        throw new Error("none inject option annotation" + annotation);
    }

    private Class<? extends Annotation> getScope(final Class<?> target)
    {
        for (final Annotation annotation : target.getAnnotations())
        {
            if (annotation.annotationType()
                    .isAnnotationPresent(ScopeAnnotation.class))
            {
                return annotation.annotationType();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> Binding<T> newBinding(final Object annotation, final Type type,
            final Class<? extends T> implementType,
            final Class<? extends Annotation> scope)
    {
        final Class<T> interfaceType = (Class<T>) type;
        return new Binding<T>()
        {
            @Override
            public Object annotation()
            {
                return annotation;
            }

            @Override
            public Class<? extends T> implementType()
            {
                return implementType;
            }

            @Override
            public Class<T> interfaceType()
            {
                return interfaceType;
            }

            @Override
            public Provider<T> provider()
            {
                return instanceMap.getProvider(this);
            }

            @Override
            public Class<? extends Annotation> scope()
            {
                return scope;
            }
        };
    }

    private <T> Binding<T> pickupBinding(final Type type,
            final Annotation annotation)
    {
        return getBindingAnnotatedMap(annotation).get(type, annotation);
    }

    private Class<? extends Annotation> pickupScope(
            final Annotation[] annotations)
    {
        for (final Annotation annotation : annotations)
        {
            if (annotation.annotationType()
                    .isAnnotationPresent(ScopeAnnotation.class))
            {
                return annotation.annotationType();
            }
        }
        return Prototype.class;
    }

    private <T> Binding<T> put(final Type type, final Annotation annotation,
            final Binding<T> binding)
    {
        getBindingAnnotatedMap(annotation).put(type, annotation, binding);
        return binding;
    }

    @SuppressWarnings("unchecked")
    private synchronized <T> Binding<T> setInterfaceAnnotateBinding(
            final Type type, final Annotation parameterAnnotation)
    {
        final Binding<T> binding = pickupBinding(type, parameterAnnotation);
        if (binding != null)
        {
            return binding;
        }
        if (type instanceof Class<?>)
        {
            final Class<?> target = (Class<?>) type;
            final ImplementedBy implementedBy = target.getAnnotation(ImplementedBy.class);
            if (implementedBy != null)
            {
                final Class<?> to = implementedBy.value();
                return bindImplement(type,
                        (Class<? extends T>) to,
                        null,
                        getScope(to));
            }
            final ProvidedBy providedBy = target.getAnnotation(ProvidedBy.class);
            if (providedBy != null)
            {
                final Class<?> to = providedBy.value();
                return bindProviderImplement(type,
                        (Class<? extends Provider<T>>) to,
                        null,
                        getScope(to));
            }
        }
        throw new Error("none binding" + type);
    }

    @SuppressWarnings("unchecked")
    private synchronized <T> Binding<T> setNoneInterfaceBinding(
            final Type type, final Annotation annotation)
    {
        final Binding<T> binding = pickupBinding(type, annotation);
        if (binding != null)
        {
            return binding;
        }

        final Class<? extends Annotation> scope = pickupScope(((Class<?>) type).getAnnotations());
        final Binding<T> newBinding = newBinding(annotation,
                type,
                (Class<T>) type,
                scope);
        put(type, annotation, newBinding);
        return newBinding;
    }

    /**
     * default constractor
     * 
     * @param scopeMap
     */
    public BindingMapImpl(final ScopeMap scopeMap)
    {
        this.instanceMap = new InstanceMapImpl(scopeMap, this);
        this.map = new HashMap<Class<?>, BindingAnnotatedMap<Annotation>>();
        this.map.put(noneAnnotate.getClass(), noneAnnotatedMap);
        final BindingMap bindingMap = this;
        this.injector = new Injector()
        {

            @Override
            public <T> T getInstance(final Class<T> type)
            {
                return getProvider(type).get();
            }

            @Override
            public <T> Provider<T> getProvider(final Class<T> type)
            {
                final Binding<T> binding = bindingMap.getBinding(type, null);
                final Provider<T> provider = instanceMap.getProvider(binding);
                return provider;
            }

            @Override
            public <T> T injectMember(final T target)
            {
                return instanceMap.injectMember(target);
            }
        };
    }

    private final Injector injector;
    private final InstanceMap instanceMap;
    private final HashMap<Class<?>, BindingAnnotatedMap<Annotation>> map;
    private final Annotation noneAnnotate = new Annotation()
    {
        @Override
        public Class<? extends Annotation> annotationType()
        {
            return this.getClass();
        }
    };
    private final BindingAnnotatedMap<Annotation> noneAnnotatedMap = new BindingAnnotatedMap<Annotation>()
    {
        @SuppressWarnings("unchecked")
        @Override
        public <R> Binding<R> get(final Type type, final Annotation annotation)
        {
            return (Binding<R>) map.get(type);
        }

        @Override
        public <B> void put(final Type type, final Annotation annotation,
                final Binding<B> binding)
        {
            map.put(type, binding);
        }

        private final HashMap<Type, Binding<?>> map = new HashMap<Type, Binding<?>>();
    };
}