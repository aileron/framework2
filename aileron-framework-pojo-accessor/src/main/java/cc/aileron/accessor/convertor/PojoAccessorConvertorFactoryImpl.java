/*
 * Copyright (C) 2008 aileron.cc
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
package cc.aileron.accessor.convertor;

import static cc.aileron.generic.util.Cast.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.instance.InstanceManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * PojoConvertorFactory 実装
 * 
 * @author Aileron
 * 
 */
@Singleton
class PojoAccessorConvertorFactoryImpl implements PojoAccessorConvertorFactory
{
    interface Factory<T>
    {
        /**
         * @param object
         * @return convertor
         */
        PojoAccessorConvertor create(T object);
    }

    @Override
    public PojoAccessorConvertor create(final Object object)
    {
        if (object == null)
        {
            return null;
        }

        final Factory<Object> factory = cast(factorys.get(object.getClass()));
        if (factory == null)
        {
            return null;
        }
        return factory.create(object);
    }

    /**
     * constractor injection
     * 
     * @param accessor
     * @param instance
     */
    @Inject
    public PojoAccessorConvertorFactoryImpl(final PojoAccessorManager accessor,
            final InstanceManager instance)
    {
        final Factory<Map<String, ?>> mapConvertor = new Factory<Map<String, ?>>()
        {
            @Override
            public PojoAccessorConvertor create(final Map<String, ?> p)
            {
                return new PojoAccessorConvertor()
                {
                    @Override
                    public <T> T to(final Class<T> to)
                            throws PojoAccessorValueNotFoundException,
                            PojoPropertiesNotFoundException
                    {
                        final PojoAccessor<T> pojo = accessor.from(instance.get(to));
                        for (final Entry<String, ?> e : p.entrySet())
                        {
                            pojo.to(e.getKey()).value(e.getValue());
                        }
                        return pojo.toTarget();
                    }
                };
            }
        };
        factorys.put(Map.class, mapConvertor);
        factorys.put(HashMap.class, mapConvertor);
        factorys.put(Properties.class, mapConvertor);
    }

    /**
     * factorys
     */
    private final HashMap<Class<?>, Factory<?>> factorys = new HashMap<Class<?>, Factory<?>>();
}