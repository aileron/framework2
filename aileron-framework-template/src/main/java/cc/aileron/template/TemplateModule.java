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

import cc.aileron.template.comment.BlockComment;
import cc.aileron.template.compiler.TemplateCompilerImpl;
import cc.aileron.template.parser.EscapeMethod;
import cc.aileron.template.parser.ParserMethodProviderMap;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 
 * Template-module
 * 
 * @author Aileron
 * 
 */
public class TemplateModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.install(module);
        binder.bind(TemplateCompiler.class)
                .to(TemplateCompilerImpl.class)
                .asEagerSingleton();
    }

    /**
     * @param configure
     */
    public TemplateModule(final TemplateConfigure configure)
    {
        module = new Module()
        {
            @Override
            public void configure(final Binder binder)
            {
                binder.bind(BlockComment.class)
                        .to(configure.blockComment())
                        .asEagerSingleton();
                binder.bind(ParserMethodProviderMap.class)
                        .to(configure.parserMethodProviderMap())
                        .asEagerSingleton();
                binder.bind(EscapeMethod.class)
                        .to(configure.escapeMethod())
                        .asEagerSingleton();
            }
        };
    }

    private final Module module;
}
