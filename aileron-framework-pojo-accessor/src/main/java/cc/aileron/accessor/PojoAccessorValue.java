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

import java.lang.reflect.Type;
import java.util.List;

import cc.aileron.generic.PrimitiveWrappers.NumberGetAccessor;

/**
 * @author Aileron
 */
public interface PojoAccessorValue extends NumberGetAccessor
{
    /**
     * @param <T>
     * @param type
     * @return accessor
     */
    <T> PojoAccessor<T> accessor(Class<T> type);

    /**
     * @param <T>
     * @param type
     * @return iterable-accessor
     */
    <T> Iterable<PojoAccessor<T>> accessorIterable(Class<T> type);

    /**
     * @param method
     * @return exist
     */
    boolean exist(PojoAccessorMethod method);

    /**
     * @param method
     * @return {@link Type}
     */
    Type genericType(PojoAccessorMethod method);

    /**
     * @param <T>
     * @param type
     * @return iterable
     */
    <T> Iterable<T> iterable(Class<T> type);

    /**
     * @param <T>
     * @param type
     * @return list
     */
    <T> List<T> list(Class<T> type);

    /**
     * @return {@link Object}
     */
    Object toObject();

    /**
     * @param method
     * @return type
     */
    Class<?> type(PojoAccessorMethod method);

    /**
     * @param <T>
     * @param type
     * @return value
     */
    <T> T value(Class<T> type);

    /**
     * @param value
     */
    void value(Object value);
}