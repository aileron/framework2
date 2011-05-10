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

package cc.aileron.dao.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.accessor.convertor.PojoAccessorConvertorFactory;
import cc.aileron.commons.resource.Resource;
import cc.aileron.commons.util.ResourceUtils;
import cc.aileron.dao.G2DaoDataSource;

import com.google.inject.Guice;
import com.google.inject.Provider;

/**
 * G2DataSource
 *
 * @author Aileron
 */
public class G2DaoConnectionProvider implements Provider<Connection>,
        DataSource
{
    /**
     * @author aileron
     */
    public static interface Factory
    {
        /**
         * @param resource
         * @return {@link G2DaoConnectionProvider}
         * @throws IOException 
         * @throws PojoPropertiesNotFoundException 
         * @throws PojoAccessorValueNotFoundException 
         */
        G2DaoConnectionProvider get(Resource resource)
                throws PojoAccessorValueNotFoundException,
                PojoPropertiesNotFoundException, IOException;
    }

    /**
     * @param resourceName
     * @return {@link DataSource}
     */
    public static DataSource dataSource(final String resourceName)
    {
        try
        {
            return new G2DaoConnectionProvider(G2DaoDataSource.class,
                    ResourceUtils.resource(resourceName).toProperties());
        }
        catch (final Exception e)
        {
            throw new Error(e);
        }
    }

    /**
     * @param datasourceClass
     * @return {@link Factory}
     */
    public static Factory get(final Class<? extends DataSource> datasourceClass)
    {
        return new Factory()
        {
            @Override
            public G2DaoConnectionProvider get(final Resource resource)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException, IOException
            {
                return new G2DaoConnectionProvider(datasourceClass,
                        resource.toProperties());
            }
        };
    }

    @Override
    public Connection get()
    {
        try
        {
            return dataSource.getConnection();
        }
        catch (final SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(final String username, final String password)
            throws SQLException
    {
        return dataSource.getConnection(username, password);
    }

    @Override
    public int getLoginTimeout() throws SQLException
    {
        return dataSource.getLoginTimeout();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException
    {
        return dataSource.getLogWriter();
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException
    {
        return dataSource.isWrapperFor(iface);
    }

    @Override
    public void setLoginTimeout(final int seconds) throws SQLException
    {
        dataSource.setLoginTimeout(seconds);
    }

    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException
    {
        dataSource.setLogWriter(out);
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException
    {
        return dataSource.unwrap(iface);
    }

    /**
     * @param dataSourceClass
     * @param properties
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    public G2DaoConnectionProvider(
            final Class<? extends DataSource> dataSourceClass,
            final Properties properties)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        this.dataSource = Guice.createInjector()
                .getInstance(PojoAccessorConvertorFactory.class)
                .create(properties)
                .to(dataSourceClass);
    }

    /**
     * @param dataSourceClass
     * @param resource 
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     * @throws IOException 
     */
    public G2DaoConnectionProvider(
            final Class<? extends DataSource> dataSourceClass,
            final Resource resource) throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException, IOException
    {
        this.dataSource = Guice.createInjector()
                .getInstance(PojoAccessorConvertorFactory.class)
                .create(resource.toProperties())
                .to(dataSourceClass);
    }

    /**
     * @param dataSource
     */
    public G2DaoConnectionProvider(final DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    /**
     * ds
     */
    private final DataSource dataSource;
}
