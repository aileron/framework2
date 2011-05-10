/**
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ジェネリクスなクラスの型パラメータを取得する為のアクセッサ
 * 
 * @author Aileron
 */
public class GenericClassAccessor
{
    /**
     * @param interfaceClass
     * @return 型パラメータ
     */
    public Class<?>[] getInterfaceClassParameters(final Class<?> interfaceClass)
    {
        if (interfaceClass == null)
        {
            throw new IllegalArgumentException("null value is interfaceClass");
        }
        for (final Type t : targetClass.getGenericInterfaces())
        {
            if (!(t instanceof ParameterizedType))
            {
                continue;
            }
            final ParameterizedType type = (ParameterizedType) t;
            if (interfaceClass.equals(type.getRawType()))
            {
                return (Class[]) type.getActualTypeArguments();
            }
        }
        throw new NullPointerException();
    }

    /**
     * @return クラスの型パラメータ
     */
    public Class<?>[] getSuperClassParameters()
    {
        final Type type = targetClass.getGenericSuperclass();
        if (!(type instanceof ParameterizedType))
        {
            throw new NullPointerException();
        }
        final ParameterizedType pType = (ParameterizedType) type;
        return (Class<?>[]) pType.getActualTypeArguments();
    }

    /**
     * constractor
     * 
     * @param targetClass
     */
    public GenericClassAccessor(final Class<?> targetClass)
    {
        this.targetClass = targetClass;
    }

    /**
     * constractor
     * 
     * @param target
     */
    public GenericClassAccessor(final Object target)
    {
        this.targetClass = target.getClass();
    }

    /**
     * 対象のクラス
     */
    private final Class<?> targetClass;
}
