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
package cc.aileron.dao.db.sql;

import cc.aileron.template.TemplateConfigure;
import cc.aileron.template.comment.BlockComment;
import cc.aileron.template.comment.BlockCommentJson;
import cc.aileron.template.parser.EscapeMethod;
import cc.aileron.template.parser.ParserMethodProviderMap;

/**
 * @author Aileron
 */
public class SqlTemplateConfigure implements TemplateConfigure
{
    /**
     * configure
     */
    public static final TemplateConfigure configure = new SqlTemplateConfigure();

    @Override
    public Class<? extends BlockComment> blockComment()
    {
        return BlockCommentJson.class;
    }

    @Override
    public Class<? extends EscapeMethod> escapeMethod()
    {
        return EscapeMethodSql.class;
    }

    @Override
    public Class<? extends ParserMethodProviderMap> parserMethodProviderMap()
    {
        return SqlParserMethodProviderMap.class;
    }
}
