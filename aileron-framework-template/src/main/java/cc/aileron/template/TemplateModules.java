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
import java.util.Map.Entry;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

/**
 * @author Aileron
 */
public class TemplateModules implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.bind(new TypeLiteral<EnumMap<TemplateCategory, TemplateCompiler>>()
        {
        })
                .toInstance(map);

        binder.bind(TemplateCompilerMap.class)
                .toInstance((new TemplateCompilerMap(map)));
    }

    /**
     * constractor
     */
    public TemplateModules()
    {
        for (final Entry<TemplateCategory, TemplateConfigure> e : TemplateConfigure.configures.entrySet())
        {
            map.put(e.getKey(),
                    Guice.createInjector(new TemplateModule(e.getValue()))
                            .getInstance(TemplateCompiler.class));

        }
    }

    /**
     * map
     */
    private final EnumMap<TemplateCategory, TemplateCompiler> map = new EnumMap<TemplateCategory, TemplateCompiler>(TemplateCategory.class);
}
