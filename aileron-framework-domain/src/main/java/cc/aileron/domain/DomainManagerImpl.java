/**
 *
 */
package cc.aileron.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.generic.util.Cast;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class DomainManagerImpl implements DomainManager
{
    private enum MethodName
    {
        entity, instance
    }

    @Override
    public <Domain> DomainFactory<Domain> from(final Class<Domain> domainClass)
    {
        return new DomainFactory<Domain>()
        {
            @Override
            public <Entity> Domain get(final Entity entity) throws DomainInitializedError
            {
                return DomainManagerImpl.this.<Domain, Entity> get(domainClass, entity);
            }
        };
    }

    @Override
    public <Domain, Entity> Domain get(final Class<Domain> domainClass,
            final Entity entity) throws DomainInitializedError
    {
        if (entity == null)
        {
            throw DomainInitializedError.notFoundEntity(domainClass);
        }

        final ImplementedByDomain annotation = domainClass.getAnnotation(ImplementedByDomain.class);
        if (annotation == null)
        {
            throw DomainInitializedError.notFoundAnnotation(domainClass);
        }

        final Class<Entity> eclass = Cast.<Class<Entity>> cast(entity.getClass());
        final Class<Domain> implementDomainClass = Cast.<Class<Domain>> cast(annotation.value());
        for (final Constructor<?> c : implementDomainClass.getConstructors())
        {
            final Class<?> ptype = c.getParameterTypes()[0];
            if (DomainConfigure.class == ptype)
            {
                try
                {
                    return Cast.<Domain> cast(c.newInstance(new DomainConfigure<Entity>()
                    {

                        @Override
                        public Entity entity()
                        {
                            return entity;
                        }

                        @Override
                        public <I> I instance(final Class<I> type)
                        {
                            return i.get(type);
                        }
                    }));
                }
                catch (final Throwable th)
                {
                    throw new DomainInitializedError(th);
                }
            }
            if (!DomainConfigure.class.isAssignableFrom(ptype))
            {
                continue;
            }
            final Class<?> p = (Class<?>) ((ParameterizedType) ptype.getGenericInterfaces()[0]).getActualTypeArguments()[0];
            if (eclass != p)
            {
                continue;
            }
            try
            {
                return Cast.<Domain> cast(c.newInstance(Proxy.newProxyInstance(this.getClass()
                        .getClassLoader(),
                        new Class[] { ptype },
                        new InvocationHandler()
                        {
                            @Override
                            public Object invoke(final Object proxy,
                                    final Method method, final Object[] args)
                                    throws Throwable
                            {
                                try
                                {
                                    switch (MethodName.valueOf(method.getName()))
                                    {
                                    case entity:
                                        return entity;
                                    case instance:
                                        return i.get((Class<?>) args[0]);
                                    }
                                }
                                catch (final IllegalArgumentException e)
                                {
                                }
                                throw new UnsupportedOperationException();
                            }
                        })));
            }
            catch (final Throwable th)
            {
                throw new DomainInitializedError(th);
            }
        }
        throw DomainInitializedError.notFoundConstractor(implementDomainClass,
                entity.getClass());
    }

    /**
     * @param i
     */
    @Inject
    public DomainManagerImpl(final InstanceManager i)
    {
        this.i = i;
    }

    /**
     * instance-manager
     */
    final InstanceManager i;

    /**
     * constructor-map
     */
    final HashMap<Class<?>, HashMap<Class<?>, Constructor<?>>> map = new HashMap<Class<?>, HashMap<Class<?>, Constructor<?>>>();
}
