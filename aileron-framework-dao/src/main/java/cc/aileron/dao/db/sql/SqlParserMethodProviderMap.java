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

import java.util.HashMap;
import java.util.List;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.generic.util.Cast;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.context.WriterNameSpace;
import cc.aileron.template.parser.ParserMethod;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.parser.ParserMethodProvider;
import cc.aileron.template.parser.ParserMethodProviderMap;
import cc.aileron.template.parser.method.PlainTextParserProvider;

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
public class SqlParserMethodProviderMap implements ParserMethodProviderMap
{
    @Override
    public ParserMethodProvider get(final String methodName)
            throws ParserMethodNotFoundException
    {
        final ParserMethodProvider methodProvider = map.get(methodName.toUpperCase());
        if (methodProvider == null)
        {
            throw new ParserMethodNotFoundException(methodName);
        }
        return methodProvider;
    }

    /**
     * @param factory
     * @param category
     * @return method
     */
    private ParserMethodProvider getMethod(
            final G2DaoDtoAccessor.Category category)
    {
        return new ParserMethodProvider()
        {
            @Override
            public ParserMethod get(final String content, final String args)
            {
                return new ParserMethod()
                {
                    @Override
                    public void call(final TemplateContext context)
                            throws PojoAccessorValueNotFoundException,
                            PojoPropertiesNotFoundException
                    {
                        final WriterNameSpace namespace = context.getNameSpace();
                        final PojoAccessor<Object> accessor = Cast.<PojoAccessor<Object>> cast(context.getAccessor());
                        final List<Object> parameters = context.getAccessor(1)
                                .to("parameters")
                                .list(Object.class);
                        category.call(namespace,
                                accessor,
                                parameters,
                                args,
                                content);
                    }
                };
            }
        };
    }

    /**
     * @param instance
     */
    @Inject
    public SqlParserMethodProviderMap(final InstanceManager instance)
    {
        map.put("plainText".toUpperCase(),
                instance.get(PlainTextParserProvider.class));
        for (final G2DaoDtoAccessor.Category category : G2DaoDtoAccessor.Category.values())
        {
            map.put(category.name().toUpperCase(), getMethod(category));
        }
    }

    /**
     * method-classes
     */
    private final HashMap<String, ParserMethodProvider> map = new HashMap<String, ParserMethodProvider>();
}