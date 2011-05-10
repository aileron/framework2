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
package cc.aileron.dao.db.exec;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.dao.db.G2DaoSqlMap;

import com.google.inject.ImplementedBy;

/**
 * @author Aileron
 */
@ImplementedBy(G2DaoExecutorFactoryImpl.class)
public interface G2DaoExecutorFactory
{
    /**
     * @param <T>
     * @param targetClass
     * @param serial
     * @param sqlMap
     * @param provider
     * @return executor
     */
    <T> G2DaoExecuteInvoker<T> get(final Class<T> targetClass,
            final G2DaoSqlMap sqlMap, final InstanceManager provider);
}