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
package cc.aileron.template.parser.method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.template.TemplateCategory;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.parser.ParserMethod;
import cc.aileron.template.parser.ParserMethodProvider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * var - method 特殊
 * 
 * @author Aileron
 */
@Singleton
public class ExtractVarMethodParserProvider implements ParserMethodProvider
{
    @Override
    public ParserMethod get(final String content, final String argv)
    {
        return new ParserMethod()
        {
            @Override
            public void call(final TemplateContext context)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                final String var = new VariableExpand(context.getAccessor()).expand(argv);
                logger.trace("call#var : {}", var);
                if (var != null)
                {
                    context.getNameSpace().append(var);
                }
            }
        };
    }

    /**
     * @param category 
     */
    @Inject
    public ExtractVarMethodParserProvider(final TemplateCategory category)
    {
    }

    /**
     * logger
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());
}
