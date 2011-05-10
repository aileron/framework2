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

import cc.aileron.template.comment.BlockCommentJson;
import cc.aileron.template.comment.BlockCommentXml;
import cc.aileron.template.parser.EscapeMethodJson;
import cc.aileron.template.parser.EscapeMethodXml;
import cc.aileron.template.parser.ParserMethodProviderMapImpl;

/**
 * @author Aileron
 */
public enum TemplateCategory
{
    /**
     * JSON
     */
    JSON,

    /**
     * XML
     */
    XML;

    static
    {
        TemplateConfigure.configures.put(TemplateCategory.JSON,
                new TemplateConfigureImpl(BlockCommentJson.class,
                        ParserMethodProviderMapImpl.class,
                        EscapeMethodJson.class));
        TemplateConfigure.configures.put(TemplateCategory.XML,
                new TemplateConfigureImpl(BlockCommentXml.class,
                        ParserMethodProviderMapImpl.class,
                        EscapeMethodXml.class));
    }

    /**
     * @return configure
     */
    public TemplateConfigure configure()
    {
        return TemplateConfigure.configures.get(this);
    }
}
