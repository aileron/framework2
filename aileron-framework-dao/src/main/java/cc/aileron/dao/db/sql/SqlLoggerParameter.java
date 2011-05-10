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
package cc.aileron.dao.db.sql;

import java.util.List;

/**
 * @author Aileron
 * 
 */
public class SqlLoggerParameter
{
    /**
     * constractor set parameters
     * 
     * @param name
     * @param sql
     * @param arguments
     */
    public SqlLoggerParameter(
        final String name,
        final String sql,
        final List<Object> arguments)
    {
        this.name = name;
        this.sql = sql;
        this.arguments = arguments;
    }

    /**
     * メソッド名
     */
    public final String name;

    /**
     * bindされる前のSQL
     */
    public final String sql;

    /**
     * bind パラメータ
     */
    public final List<Object> arguments;
}
