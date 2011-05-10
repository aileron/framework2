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

import cc.aileron.dao.db.G2DbName;

import com.google.inject.Provider;

/**
 * @author Aileron
 */
public interface G2DaoTransactionManager extends Provider<Connection>
{
    /**
     * @author Aileron
     */
    enum Category
    {
        /**
         * commit
         */
        COMMIT

        /**
         * rollback
         */
        , ROLLBACK;
    }

    /**
     * @throws SQLException
     */
    void close() throws SQLException;

    /**
     * @param connection
     */
    void close(Connection connection);

    /**
     * @return {@link G2DbName}
     */
    G2DbName db();

    /**
     * @param category
     * @throws SQLException
     */
    void end(Category category) throws SQLException;

    /**
     */
    void start();
}
