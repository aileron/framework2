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
 * フレームワーク内部で使用するインタフェース
 * 
 * @author Aileron
 * @param <T>
 */
public interface G2DaoLocal<T> extends G2Dao<T>
{
    /**
     * @param instance
     * @param transactionManager
     */
    void initialize(InstanceManager instance,
            G2DaoTransactionManager transactionManager);
}