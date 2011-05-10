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

import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.flow.FlowCategory;
import cc.aileron.template.flow.FlowDefMethod;
import cc.aileron.template.flow.FlowMethodProvider;
import cc.aileron.template.flow.FlowMethodProviderMap;
import cc.aileron.template.parser.ParserMethod;
import cc.aileron.template.parser.ParserMethodProvider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class DefRepMethodParserProvider implements ParserMethodProvider
{
    @Override
    public ParserMethod get(final String content, final String args)
    {
        final int point = args.indexOf('&');
        final String defarg = args.substring(0, point), repargs = args.substring(point + 1);
        return new ParserMethod()
        {
            @Override
            public void call(final TemplateContext context)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                if (def.call(context))
                {
                    rep.call(context);
                }
                else
                {
                    context.getNameSpace().append(content);
                }
            }

            final FlowDefMethod def = (FlowDefMethod) pdef.get(defarg);
            final ParserMethod rep = prep.get(content, repargs);
        };
    }

    /**
     * @param fmap 
     * @param prep 
     */
    @Inject
    public DefRepMethodParserProvider(final FlowMethodProviderMap fmap,
            final RepMethodParserProvider prep)
    {
        pdef = (fmap.get(FlowCategory.DEF));
        this.prep = prep;
    }

    final FlowMethodProvider pdef;
    final RepMethodParserProvider prep;
}
