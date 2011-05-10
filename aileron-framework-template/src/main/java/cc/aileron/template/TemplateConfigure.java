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
package cc.aileron.template;

import java.util.EnumMap;

import cc.aileron.template.comment.BlockComment;
import cc.aileron.template.parser.EscapeMethod;
import cc.aileron.template.parser.ParserMethodProviderMap;

/**
 * @author Aileron
 */
public interface TemplateConfigure
{
    /**
     * configures
     */
    public static EnumMap<TemplateCategory, TemplateConfigure> configures = new EnumMap<TemplateCategory, TemplateConfigure>(TemplateCategory.class);

    /**
     * @return BlockComment
     */
    Class<? extends BlockComment> blockComment();

    /**
     * @return EscapeMethod
     */
    Class<? extends EscapeMethod> escapeMethod();

    /**
     * @return ParserMethodProviderMap
     */
    Class<? extends ParserMethodProviderMap> parserMethodProviderMap();
}

class TemplateConfigureImpl implements TemplateConfigure
{
    @Override
    public Class<? extends BlockComment> blockComment()
    {
        return blockComment;
    }

    @Override
    public Class<? extends EscapeMethod> escapeMethod()
    {
        return escapeMethod;
    }

    @Override
    public Class<? extends ParserMethodProviderMap> parserMethodProviderMap()
    {
        return parserMethodProviderMap;
    }

    TemplateConfigureImpl(
            final Class<? extends BlockComment> blockComment,
            final Class<? extends ParserMethodProviderMap> parserMethodProviderMap,
            final Class<? extends EscapeMethod> escapeMethod)
    {
        this.blockComment = blockComment;
        this.parserMethodProviderMap = parserMethodProviderMap;
        this.escapeMethod = escapeMethod;
    }

    private final Class<? extends BlockComment> blockComment;
    private final Class<? extends EscapeMethod> escapeMethod;
    private final Class<? extends ParserMethodProviderMap> parserMethodProviderMap;

}