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
package cc.aileron.template.compiler;

import java.io.PrintWriter;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.template.Template;
import cc.aileron.template.TemplateCompiler;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.context.TemplateContextFactory;
import cc.aileron.template.context.TemplateInterpreter;
import cc.aileron.template.flow.FlowExecutor;
import cc.aileron.template.flow.FlowMethodNotFoundError;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateReader;
import cc.aileron.template.reader.TemplateSyntaxEexception;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * 実装
 * 
 * @author Aileron
 */
@Singleton
public class TemplateCompilerImpl implements TemplateCompiler
{
    @Override
    public Template compile(final CharSequence charSequence)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException
    {
        final TemplateReader reader = templateReader;
        final TemplateContextFactory contextFactory = templateContextFactory;
        final TemplateInterpreter interpreter = flowExecutor.get()
                .setComponnent(reader.read(charSequence));
        return new Template()
        {
            @Override
            public TemplateContext print(final PrintWriter writer,
                    final PojoAccessor<?>... accessor)
                    throws FlowMethodNotFoundError,
                    PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                final TemplateContext context = contextFactory.get(accessor);
                interpreter.call(context);
                writer.print(context.getNameSpace().toOutputStrings());
                writer.flush();
                return context;
            }

            @Override
            public String toString()
            {
                return charSequence.toString();
            }
        };
    }

    /**
     * @param templateReader
     * @param templateContextFactory
     * @param flowExecutor 
     */
    @Inject
    public TemplateCompilerImpl(final TemplateReader templateReader,
            final TemplateContextFactory templateContextFactory,
            final Provider<FlowExecutor> flowExecutor)
    {
        this.templateReader = templateReader;
        this.templateContextFactory = templateContextFactory;
        this.flowExecutor = flowExecutor;
    }

    private final Provider<FlowExecutor> flowExecutor;
    private final TemplateContextFactory templateContextFactory;
    private final TemplateReader templateReader;
}
