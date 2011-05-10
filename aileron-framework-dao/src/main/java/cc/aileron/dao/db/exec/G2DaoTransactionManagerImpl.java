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
public class G2DaoTransactionManagerImpl implements G2DaoTransactionManager
{
    @Override
    public void close() throws SQLException
    {
        if (--local().count != 0)
        {
            return;
        }
        local().connection.close();
        local(new TransactionLocal());
    }

    @Override
    public void close(final Connection connection)
    {
        if (local().count != 0)
        {
            return;
        }
        try
        {
            if (connection.isClosed())
            {
                return;
            }
            connection.close();
        }
        catch (final SQLException e)
        {
            throw new Error(e);
        }
    }

    @Override
    public G2DbName db()
    {
        return dbName;
    }

    @Override
    public void end(final Category category) throws SQLException
    {
        switch (category)
        {
        case COMMIT:
            local().connection.commit();
            break;

        case ROLLBACK:
            local().connection.rollback();
            break;
        }
    }

    @Override
    public Connection get()
    {
        final Connection connection = local().connection;
        if (connection != null)
        {
            return local().connection;
        }
        return connectionProvider.get();
    }

    @Override
    public void start()
    {
        if (local().count == 0)
        {
            local().connection = connectionProvider.get();
            try
            {
                local().connection.setAutoCommit(false);
            }
            catch (final SQLException e)
            {
                throw new Error(e);
            }
        }
        local().count += 1;
    }

    /**
     * @return db
     * @throws SQLException
     */
    private G2DbName getDbName(final Provider<Connection> connection)
            throws SQLException
    {
        final String name = connection.get()
                .getMetaData()
                .getDatabaseProductName();

        final G2DbName dbName = G2DbName.convert(name);
        return dbName;
    }

    /**
     * @return local
     */
    private TransactionLocal local()
    {
        return local.get();
    }

    /**
     * @param newValue
     */
    private void local(final TransactionLocal newValue)
    {
        local.set(newValue);
    }

    /**
     * @param connectionProvider
     * @throws SQLException
     */
    public G2DaoTransactionManagerImpl(
            final Provider<Connection> connectionProvider) throws SQLException
    {
        this.connectionProvider = connectionProvider;
        this.dbName = getDbName(connectionProvider);
    }

    /**
     * connectionProvider
     */
    private final Provider<Connection> connectionProvider;

    /**
     * db-name
     */
    private final G2DbName dbName;

    /**
     * local
     */
    private final ThreadLocal<TransactionLocal> local = new ThreadLocal<TransactionLocal>()
    {
        @Override
        protected TransactionLocal initialValue()
        {
            return new TransactionLocal();
        }
    };
}

/**
 * @author Aileron
 */
class TransactionLocal
{
    /**
     * connections
     */
    public Connection connection;

    /**
     * count
     */
    public int count = 0;
}