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
package cc.aileron.dao;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.dao.db.exec.G2DaoTransactionManager;

/**
 * @author Aileron
 */
public interface G2DaoRegistrar
{
    /**
     * @param <T>
     * @param targetClass
     * @return {@link G2DaoLocal}
     */
    <T> G2DaoLocal<T> get(final Class<T> targetClass);

    /**
     * @param instance
     * @param transactionManager
     * @return {@link G2DaoRegistrar}
     */
    G2DaoRegistrar initialize(final InstanceManager instance,
            G2DaoTransactionManager transactionManager);

    /**
     * @param <T>
     * @param type
     * @param dao
     */
    <T> void registry(final Class<T> type, final G2DaoLocal<T> dao);
}
