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
import java.sql.ResultSet;
import java.sql.SQLException;

import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.dao.db.G2DaoNoCondition;
import cc.aileron.dao.db.G2DaoResultHandler;
import cc.aileron.dao.db.G2DaoSerial;
import cc.aileron.dao.db.G2DaoSqlMap;
import cc.aileron.dao.db.G2DbName;
import cc.aileron.template.flow.FlowMethodNotFoundError;

/**
 * @author Aileron
 * @param <T>
 * 
 */
public class G2DaoExecuteInvokerImpl<T> implements G2DaoExecuteInvoker<T>
{
    @Override
    public Number execute(final G2DbName dbName, final Connection connection,
            final Class<?> type, final Object condition,
            final Category category, final T bean)
            throws SQLException, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final PreparedStatement statement;
        switch (category)
        {
        case INITIALIZE:
            statement = statementFactory.create(dbName,
                    "initialize",
                    connection,
                    condition);
            break;

        case INSERT:
            if (condition == G2DaoNoCondition.NO_CONDITION)
            {
                statement = statementFactory.createInsertStatment(dbName,
                        "insert",
                        connection,
                        bean);
            }
            else
            {
                final String insertSqlName = category.name().toLowerCase()
                        + sqlName.get(condition, type);
                statement = statementFactory.createInsertStatment(dbName,
                        insertSqlName,
                        connection,
                        new G2DaoObject(condition, bean));
            }
            break;

        case UPDATE:
            final String updateSqlName;
            if (condition == G2DaoNoCondition.NO_CONDITION)
            {
                updateSqlName = category.name().toLowerCase();
            }
            else
            {
                updateSqlName = category.name().toLowerCase()
                        + sqlName.get(condition, type);
            }
            statement = statementFactory.create(dbName,
                    updateSqlName,
                    connection,
                    new G2DaoObject(condition, bean));
            break;

        case EXECUTE:
            final String executeSqlName = category.name().toLowerCase()
                    + sqlName.get(condition, type);
            statement = statementFactory.create(dbName,
                    executeSqlName,
                    connection,
                    condition);
            break;

        case DELETE:
            final String name = category.name().toLowerCase()
                    + sqlName.get(condition, type);
            statement = statementFactory.create(dbName,
                    name,
                    connection,
                    condition);
            break;

        default:
            throw new Error("case式が不正です");
        }

        try
        {
            if (statement.execute())
            {
                return 0;
            }
            if (Category.INSERT == category)
            {
                final G2DaoSerial serial = i.get(dbName.getSerial());
                return serial.getSerialNumber(statement);
            }
            return statement.getUpdateCount();
        }
        finally
        {
            statement.close();
        }
    }

    @Override
    public void execute(final G2DbName dbName, final Connection connection,
            final Class<?> type, final Object condition,
            final G2DaoResultHandler handler)
            throws SQLException, G2DaoExecuteException,
            FlowMethodNotFoundError, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final String name = handler.method().sqlFileNamePrefix()
                + sqlName.get(condition, type);

        final PreparedStatement statement = handler.isEach() ? statementFactory.createEachStatment(dbName,
                name,
                connection,
                condition)
                : statementFactory.createSelectStatment(dbName,
                        name,
                        connection,
                        condition);

        if (statement.execute() == false)
        {
            statement.close();
            return;
        }

        final ResultSet rs = statement.getResultSet();
        if (rs == null)
        {
            statement.close();
            return;
        }
        try
        {
            handler.execute(rs);
        }
        finally
        {
            rs.close();
            statement.close();
        }
    }

    /**
     * @param targetClass
     * @param sqlMap
     * @param i
     */
    public G2DaoExecuteInvokerImpl(final Class<T> targetClass,
            final G2DaoSqlMap sqlMap, final InstanceManager i)
    {
        this.i = i;
        this.sqlName = i.get(G2DaoSqlName.class);
        this.statementFactory = i.get(PreparedStatementProvider.class)
                .get(targetClass, sqlMap, i);

    }

    /**
     * i
     */
    private final InstanceManager i;

    /**
     * sql-name
     */
    private final G2DaoSqlName sqlName;

    /**
     * statementFactory
     */
    private final PreparedStatementFactory<T> statementFactory;
}
