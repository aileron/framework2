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

import cc.aileron.dao.G2DaoFactory;
import cc.aileron.dao.G2DaoLocal;

import com.google.inject.Inject;

/**
 * @author Aileron
 */
public class G2DaoFactoryRdbms implements G2DaoFactory
{
    @Override
    public <T> G2DaoLocal<T> create(final Class<T> type,
            final boolean isCacheable) throws Exception
    {
        return new G2DaoImpl<T>(type, sqlMap, isCacheable);
    }

    /**
     * @param sqlMap
     */
    @Inject
    public G2DaoFactoryRdbms(final G2DaoSqlMap sqlMap)
    {
        this.sqlMap = sqlMap;
    }

    /**
     * sqlMap
     */
    private final G2DaoSqlMap sqlMap;
}