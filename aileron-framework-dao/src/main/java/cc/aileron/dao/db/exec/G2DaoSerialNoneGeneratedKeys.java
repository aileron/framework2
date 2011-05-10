/**
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.dao.db.G2DaoSerial;

import com.google.inject.Singleton;

/**
 * @author Aileron
 * 
 */
@Singleton
public class G2DaoSerialNoneGeneratedKeys implements G2DaoSerial
{
    @Override
    public Long getSerialNumber(final PreparedStatement statement)
            throws SQLException, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        Long id = getId(statement.getResultSet());
        while (statement.getMoreResults())
        {
            logger.trace("statment.getMoreResults");
            id = getId(statement.getResultSet());
        }
        return id;
    }

    /**
     * @param rs
     * @throws SQLException
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    private Long getId(final ResultSet rs)
            throws SQLException, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        if (rs == null)
        {
            logger.trace("rs is null");
            return null;
        }
        if (!rs.next())
        {
            logger.trace("rs is next false");
            return null;
        }
        try
        {
            final long result = rs.getLong(1);
            logger.trace("rs.getLong({})", result);
            return result;
        }
        finally
        {
            rs.close();
        }
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
}