/*
 * Copyright (C) 2009 aileron.cc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.aileron.accessor;

import static cc.aileron.generic.util.Cast.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.commons.instance.InstanceManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class PojoPropertiesSetManagerImpl implements PojoPropertiesSetManager
{

    @Override
    public <T> PojoProperties<T> get(final String key, final Object self,
            final List<Object> parents)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final String[] tokens = key.split("\\.");
        final int length = tokens.length;

        int i = 0;
        String name = tokens[i++];

        final Stack<PojoProperties<?>> stack = new Stack<PojoProperties<?>>();
        if (!parents.isEmpty())
        {
            for (final Object parent : parents)
            {
                stack.push(get(parent).get("self"));
            }
        }
        PojoProperties<?> c;
        if ("super".equals(name))
        {
            if (stack.isEmpty())
            {
                throw new PojoAccessorValueNotFoundException(self.getClass(),
                        name);
            }
            c = stack.pop();
        }
        else
        {
            stack.push(get(self).get("self"));
            final PojoPropertiesSet pset = get(self);
            c = pset.get(name);
        }
        while (length > i)
        {
            Object value = c.exist(PojoAccessorMethod.GET) ? c.get() : null;
            if (value == null)
            {
                final Class<?> type = c.type(PojoAccessorMethod.GET);
                try
                {
                    value = instance.get(type);
                    c.set(value);
                }
                catch (final Throwable throwable)
                {
                    logger.error("new instance error", throwable);
                    throw new PojoAccessorValueNotFoundException(type, name);
                }
            }
            name = tokens[i++];
            if (name.equals("super"))
            {
                if (stack.isEmpty())
                {
                    throw new PojoAccessorValueNotFoundException(c.type(PojoAccessorMethod.GET),
                            name);
                }
                c = stack.pop();
            }
            else
            {
                stack.push(c);
                final PojoPropertiesSet pset = get(value);
                c = pset.get(name);
            }
        }
        return cast(c);
    }

    @Override
    public <T> PojoPropertiesSet get(final T self)
    {
        final Class<T> type = cast(self.getClass());
        return getFactory(type).create(self);
    }

    /**
     * @param targetClass
     * @return set
     */
    private <T> PojoPropertiesSetFactory<T> createFactory(final Class<T> type)
    {
        final PojoPropertiesMeta<T> meta = new PojoPropertiesMetaImpl<T>(type);
        return new PojoPropertiesSetFactory<T>()
        {

            @Override
            public PojoPropertiesSet create(final T self)
            {
                return new PojoPropertiesSet()
                {
                    @Override
                    public PojoProperties<Object> get(final String key)
                            throws PojoAccessorValueNotFoundException
                    {
                        final Getter getter = meta.get(self, key);
                        final Setter setter = meta.set(self, key);

                        if (getter == null && setter == null)
                        {
                            throw new PojoAccessorValueNotFoundException(type,
                                    key);
                        }

                        return new PojoProperties<Object>()
                        {
                            @Override
                            public boolean exist(final PojoAccessorMethod method)
                            {
                                switch (method)
                                {
                                case GET:
                                    return getter != null;

                                case SET:
                                    return setter != null;
                                }
                                return false;
                            }

                            @Override
                            public Type genericType(
                                    final PojoAccessorMethod method)
                            {
                                switch (method)
                                {
                                case GET:
                                    return getter == null ? null
                                            : getter.genericType();

                                case SET:
                                    return setter == null ? null
                                            : setter.genericType();
                                }
                                throw new Error("想定したcase式以外が発生しました");
                            }

                            @Override
                            public Object get()
                            {
                                if (getter == null)
                                {
                                    throw new Error(new PojoPropertiesNotFoundException(self.getClass(),
                                            key,
                                            PojoAccessorMethod.GET));
                                }
                                return getter.get();
                            }

                            @Override
                            public PojoPropertiesMetaCategory meta(
                                    final PojoAccessorMethod method)
                            {
                                switch (method)
                                {
                                case GET:
                                    return getter == null ? null
                                            : getter.meta();
                                case SET:
                                    return setter == null ? null
                                            : setter.meta();
                                }
                                throw new Error("bug !!");
                            }

                            @Override
                            public void set(final Object value)
                            {
                                if (setter == null)
                                {
                                    throw new Error(new PojoPropertiesNotFoundException(self.getClass(),
                                            key,
                                            PojoAccessorMethod.SET));
                                }
                                setter.set(value);
                            }

                            @Override
                            public Class<Object> type(
                                    final PojoAccessorMethod method)
                            {
                                switch (method)
                                {
                                case GET:
                                    if (getter != null)
                                    {
                                        return cast(getter.resultType());
                                    }
                                    break;
                                case SET:
                                    if (setter != null)
                                    {
                                        return cast(setter.argumentType());
                                    }
                                    break;
                                }
                                throw new Error(new PojoAccessorValueNotFoundException(type,
                                        key));
                            }
                        };
                    }

                    @Override
                    public List<String> keys(final PojoAccessorMethod method)
                    {
                        return meta.keys(method);
                    }
                };
            }
        };
    }

    /**
     * @param type
     * @return set
     */
    private <T> PojoPropertiesSetFactory<T> getFactory(final Class<T> type)
    {
        final PojoPropertiesSetFactory<T> factory = cast(map.get(type));
        if (factory != null)
        {
            return factory;
        }
        final PojoPropertiesSetFactory<T> newFactory = createFactory(type);
        map.put(type, newFactory);
        return newFactory;
    }

    /**
     * @param instance
     */
    @Inject
    public PojoPropertiesSetManagerImpl(final InstanceManager instance)
    {
        this.instance = instance;
    }

    /**
     * instance
     */
    private final InstanceManager instance;

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * map
     */
    private final HashMap<Class<?>, PojoPropertiesSetFactory<?>> map = new HashMap<Class<?>, PojoPropertiesSetFactory<?>>();
}