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

import java.sql.Connection;
import java.sql.SQLException;

import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.dao.db.G2DaoResultHandler;
import cc.aileron.dao.db.G2DbName;
import cc.aileron.template.flow.FlowMethodNotFoundError;

/**
 * @author Aileron
 * @param <T>
 */
public interface G2DaoExecuteInvoker<T>
{
    /**
     * @author Aileron
     */
    enum Category
    {
        /**
         * delete
         */
        DELETE

        /**
         * execute
         */
        , EXECUTE

        /**
         * initialize
         */
        , INITIALIZE

        /**
         * insert
         */
        , INSERT

        /**
         * update
         */
        , UPDATE;
    }

    /**
     * @param dbName
     * @param connection
     * @param type
     * @param condition
     * @param category
     * @param bean
     * @return count
     * @throws SQLException
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    Number execute(G2DbName dbName, Connection connection, Class<?> type,
            Object condition, Category category, T bean)
            throws SQLException, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException;

    /**
     * @param dbName
     * @param connection
     * @param type
     * @param condition
     * @param invoker
     * @throws SQLException
     * @throws G2DaoExecuteException 
     * @throws PojoPropertiesNotFoundException 
     * @throws PojoAccessorValueNotFoundException 
     * @throws FlowMethodNotFoundError 
     */
    void execute(G2DbName dbName, Connection connection, Class<?> type,
            Object condition, G2DaoResultHandler invoker)
            throws SQLException, G2DaoExecuteException,
            FlowMethodNotFoundError, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException;
}
