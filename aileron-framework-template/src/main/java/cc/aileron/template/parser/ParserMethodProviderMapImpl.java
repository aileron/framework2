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
package cc.aileron.template.parser;

import java.util.HashMap;

import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.parser.method.DefRepMethodParserProvider;
import cc.aileron.template.parser.method.PlainTextParserProvider;
import cc.aileron.template.parser.method.RepMethodParserProvider;
import cc.aileron.template.parser.method.VarMethodParserProvider;
import cc.aileron.template.parser.method.VariableExpand;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * xml-method-map
 * 
 * @author Aileron
 * 
 */
@Singleton
public class ParserMethodProviderMapImpl implements ParserMethodProviderMap
{
    @Override
    public ParserMethodProvider get(final String methodName)
            throws ParserMethodNotFoundException
    {
        final ParserMethodProvider methodProvider = map.get(methodName);
        if (methodProvider == null)
        {
            throw new ParserMethodNotFoundException(methodName);
        }
        return methodProvider;
    }

    private ParserMethodProvider extraVar(final EscapeMethod escape)
    {
        return new ParserMethodProvider()
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
                        if (var != null)
                        {
                            context.getNameSpace().append(escape.apply(var));
                        }
                    }
                };
            }
        };
    }

    /**
     * @param provider
     * @param escape
     */
    @Inject
    public ParserMethodProviderMapImpl(final InstanceManager provider,
            final EscapeMethod escape)
    {
        map = new HashMap<String, ParserMethodProvider>();
        map.put("plainText", provider.get(PlainTextParserProvider.class));
        map.put("def-rep", provider.get(DefRepMethodParserProvider.class));
        map.put("rep", provider.get(RepMethodParserProvider.class));
        map.put("var", provider.get(VarMethodParserProvider.class));

        /*
         * extra var
         */
        map.put("evar", extraVar(escape));
        map.put("qvar", extraVar(new EscapeMethod()
        {
            @Override
            public String apply(final String source)
            {
                return "'" + escape.apply(source) + "'";
            }
        }));
        map.put("dqvar", extraVar(new EscapeMethod()
        {
            @Override
            public String apply(final String source)
            {
                return "\"" + escape.apply(source) + "\"";
            }
        }));
    }

    /**
     * method-classes
     */
    private final HashMap<String, ParserMethodProvider> map;
}