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
package cc.aileron.dao.db;

import java.io.IOException;

import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.dao.db.sql.G2DaoSqlMapImpl;
import cc.aileron.template.Template;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;

import com.google.inject.ImplementedBy;

/**
 * @author Aileron
 */
@ImplementedBy(G2DaoSqlMapImpl.class)
public interface G2DaoSqlMap
{
    /**
     * @param targetClass
     * @throws IOException
     * @throws ResourceNotFoundException
     * @throws ParserMethodNotFoundException
     * @throws TemplateSyntaxEexception
     */
    void compile(Class<?> targetClass) throws IOException,
            ResourceNotFoundException,
            TemplateSyntaxEexception,
            ParserMethodNotFoundException;

    /**
     * @param targetClass
     * @param dbName
     * @param fileName
     * @return テンプレート
     */
    Template get(Class<?> targetClass,
            G2DbName dbName,
            String fileName);
}
