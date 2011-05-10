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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.dao.db.G2DbName;
import cc.aileron.template.flow.FlowMethodNotFoundError;

/**
 * @author Aileron
 * @param <T>
 */
public interface PreparedStatementFactory<T>
{
    /**
     * @param dbName
     * @param name
     * @param connection
     * @param pojo
     * @return statement
     * @throws SQLException
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     * @throws FlowMethodNotFoundError
     */
    PreparedStatement create(G2DbName dbName, final String name,
            final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException;

    /**
     * @param dbName
     * @param name
     * @param connection
     * @param pojo
     * @return statement
     * @throws SQLException
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     * @throws FlowMethodNotFoundError
     */
    PreparedStatement createEachStatment(G2DbName dbName, final String name,
            final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException;

    /**
     * @param dbName
     * @param name
     * @param connection
     * @param pojo
     * @return statement
     * @throws SQLException
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     * @throws FlowMethodNotFoundError
     */
    PreparedStatement createInsertStatment(G2DbName dbName, final String name,
            final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException;

    /**
     * @param dbName
     * @param name
     * @param connection
     * @param pojo
     * @return statement
     * @throws SQLException
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     * @throws FlowMethodNotFoundError
     */
    PreparedStatement createSelectStatment(G2DbName dbName, final String name,
            final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException;
}
