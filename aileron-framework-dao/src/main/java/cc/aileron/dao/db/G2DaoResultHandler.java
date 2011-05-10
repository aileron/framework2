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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.dao.G2DaoMethod;
import cc.aileron.generic.util.SkipList;

/**
 * @author Aileron
 */
public abstract class G2DaoResultHandler
{
    /**
     * object に fetch させないカラム名に付与する為のプレフィックス
     */
    private static final char UNSET_COLUMN_MARKER_PREFIX = '#';

    /**
     * @param resultSet
     * @throws SQLException
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    public abstract void execute(ResultSet resultSet)
            throws SQLException, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException;

    /**
     * @return is each
     */
    public boolean isEach()
    {
        return false;
    }

    /**
     * @return is-count
     */
    public G2DaoMethod method()
    {
        return G2DaoMethod.FIND;
    }

    /**
     * @param accessor
     * @param columnNames
     * @param resultSet
     * @throws SQLException
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    protected void bind(final PojoAccessor<?> accessor,
            final List<String> columnNames, final ResultSet resultSet)
            throws SQLException, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {

        final int size = columnNames.size();
        for (int i = 0; i < size; i++)
        {
            final String key = columnNames.get(i);
            final Object val = resultSet.getObject(i + 1);
            if (key.charAt(0) == UNSET_COLUMN_MARKER_PREFIX)
            {
                continue;
            }
            accessor.to(key).value(val);
        }
    }

    /**
     * @param resultSet
     * @return ラベルの集合
     * @throws SQLException
     */
    protected List<String> getColumnNames(final ResultSet resultSet)
            throws SQLException
    {
        final SkipList<String> result = new SkipList<String>();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int count = metaData.getColumnCount();
        for (int i = 0; i < count; i++)
        {
            result.add(metaData.getColumnLabel(i + 1));
        }
        return result;
    }
}
