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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.dao.db.G2DaoSqlMap;
import cc.aileron.dao.db.G2DbName;
import cc.aileron.dao.db.sql.G2DaoDtoParameter;
import cc.aileron.dao.db.sql.SqlLogger;
import cc.aileron.dao.db.sql.SqlLoggerParameter;
import cc.aileron.generic.util.SkipList;
import cc.aileron.template.Template;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.flow.FlowMethodNotFoundError;

/**
 * @author Aileron
 * @param <T>
 */
public class PreparedStatementFactoryImpl<T> implements
        PreparedStatementFactory<T>
{
    static enum Type
    {
        EACH, EXECUTE, INSERT, SELECT
    }

    @Override
    public PreparedStatement create(final G2DbName dbName, final String name,
            final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException
    {
        return create(Type.EXECUTE, dbName, name, connection, pojo);
    }

    @Override
    public PreparedStatement createEachStatment(final G2DbName dbName,
            final String name, final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException
    {
        return create(Type.EACH, dbName, name, connection, pojo);
    }

    @Override
    public PreparedStatement createInsertStatment(final G2DbName dbName,
            final String name, final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException
    {
        return create(Type.INSERT, dbName, name, connection, pojo);
    }

    @Override
    public PreparedStatement createSelectStatment(final G2DbName dbName,
            final String name, final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException
    {
        return create(Type.SELECT, dbName, name, connection, pojo);
    }

    PreparedStatement create(final Type type, final G2DbName dbName,
            final String name, final Connection connection, final Object pojo)
            throws SQLException, FlowMethodNotFoundError,
            PojoAccessorValueNotFoundException, PojoPropertiesNotFoundException
    {
        final PojoAccessor<?> accessor = accessorFactory.from(pojo);
        final PojoAccessor<G2DaoDtoParameter> pAccessor = accessorFactory.from(new G2DaoDtoParameter(new SkipList<Object>()));
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        final Template template = sqlMap.get(targetClass, dbName, name);
        if (template == null)
        {
            throw new Error(new ResourceNotFoundException(targetClass + "@"
                    + name));
        }

        if (logger.isTraceEnabled())
        {
            final String templateString = template.toString();
            logger.trace(targetClass.getName() + "@" + name + "=>"
                    + templateString);
        }

        final TemplateContext context;
        try
        {
            context = template.print(printWriter, accessor, pAccessor);
            printWriter.flush();
            printWriter.close();
        }
        catch (final Throwable e)
        {
            throw new SQLException(template.toString(), e);
        }

        final List<Object> dparameters = context.getAccessor(1)
                .to("parameters")
                .list(Object.class);
        final String sql = stringWriter.toString();

        if (isDebug)
        {
            final SqlLoggerParameter parameter = new SqlLoggerParameter(targetClass.getName()
                    + "#" + name,
                    sql,
                    dparameters);
            sqlLogger.output(parameter);
        }

        final PreparedStatement preparedStatement;
        switch (type)
        {
        case EXECUTE:
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            break;

        case INSERT:
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            break;

        case SELECT:
            preparedStatement = connection.prepareStatement(sql,
                    java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            break;

        case EACH:
            preparedStatement = connection.prepareStatement(sql,
                    java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            break;

        default:
            throw new Error("バグです");
        }

        for (int max = dparameters.size(), i = 1; i <= max; i++)
        {
            final Object parameter = dparameters.get(i - 1);
            final Object value = typeConvertManager.convert(parameter);
            preparedStatement.setObject(i, value);
        }
        return preparedStatement;
    }

    /**
     * @param targetClass
     * @param sqlMap
     * @param provider
     */
    public PreparedStatementFactoryImpl(final Class<T> targetClass,
            final G2DaoSqlMap sqlMap, final InstanceManager provider)
    {
        this.targetClass = targetClass;
        this.accessorFactory = provider.get(PojoAccessorManager.class);
        this.sqlMap = sqlMap;
        this.sqlLogger = provider.get(SqlLogger.class);
        this.isDebug = sqlLogger.isEnabled();
        this.typeConvertManager = provider.get(G2DaoParameterConvertManager.class);
    }

    private final PojoAccessorManager accessorFactory;

    private final boolean isDebug;

    private final Logger logger = LoggerFactory.getLogger(this.getClass()
            .getName());

    private final SqlLogger sqlLogger;

    private final G2DaoSqlMap sqlMap;

    private final Class<T> targetClass;

    private final G2DaoParameterConvertManager typeConvertManager;
}
