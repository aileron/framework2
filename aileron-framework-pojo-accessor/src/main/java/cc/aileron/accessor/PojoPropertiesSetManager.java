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

import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author Aileron
 */
@ImplementedBy(PojoPropertiesSetManagerImpl.class)
public interface PojoPropertiesSetManager
{

    /**
     * @param <T>
     * @param parent
     * @param self
     * @param key
     * @param parents
     * @return properties
     * @throws PojoAccessorValueNotFoundException
     * @throws PojoPropertiesNotFoundException
     */
    <T> PojoProperties<T> get(String key, Object self, List<Object> parents)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException;

    /**
     * @param <T>
     * @param self
     * @return properties
     */
    <T> PojoPropertiesSet get(T self);
}
