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
package cc.aileron.generic.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * @author Aileron
 */
public class NullLogic
{
    /**
     * map
     */
    private static final HashMap<Class<?>, Object> _map = new HashMap<Class<?>, Object>();

    /**
     * @param <T>
     * @param type
     * @return null-logic
     */
    public static final <T> T get(final Class<T> type)
    {
        final HashMap<Class<?>, Object> map = _map;
        if (map.containsKey(type))
        {
            return Cast.<T> cast(map.get(type));
        }
        final T result = type.cast(Proxy.newProxyInstance(type.getClassLoader(),
                new Class<?>[] { type },
                new InvocationHandler()
                {
                    @Override
                    public Object invoke(final Object proxy,
                            final Method method, final Object[] args)
                            throws Throwable
                    {
                        throw new UnsupportedOperationException(type.getName()
                                + "@" + method.getName());
                    }
                }));
        map.put(type, result);
        return result;
    }
}