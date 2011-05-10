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
package cc.aileron.wsgi.extension.template;

import static cc.aileron.commons.util.ResourceUtils.*;

import java.util.EnumMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.template.Template;
import cc.aileron.template.TemplateCategory;
import cc.aileron.template.TemplateCompiler;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.environment.WorkflowEnvironment;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.Stage;

/**
 * @author Aileron
 */
@Singleton
public class TemplateProcessProviderImpl implements TemplateProcessProvider
{
    @Override
    public <Resource> cc.aileron.workflow.WorkflowProcess<Resource> get(
            final Object global, final String path, final String contentType,
            final TemplateCategory templateCategory)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException,
            ResourceNotFoundException
    {
        final boolean isDevelopment = this.isDevelopment;
        final TemplateCompiler templateCompiler = compilerMap.get(templateCategory);
        final TemplateResponse<Resource> response = responseFactory.get(global,
                contentType,
                this.environment.getEncode());
        final String debug = "template:" + path;
        if (isDevelopment == false)
        {
            final Template template = templateCompiler.compile(resource(path).toString());
            response.setTemplate(template);
            return new WorkflowProcess<Resource>()
            {
                @Override
                public void doProcess(final WorkflowActivity<Resource> activity)
                        throws Exception
                {
                    response.doResponse(activity);
                }

                @Override
                public String toString()
                {
                    return debug;
                }
            };
        }
        return new WorkflowProcess<Resource>()
        {
            @Override
            public void doProcess(final WorkflowActivity<Resource> activity)
                    throws Exception
            {
                final Template template = templateCompiler.compile(resource(path).toString());
                response.setTemplate(template);
                response.doResponse(activity);
            }

            @Override
            public String toString()
            {
                return debug;
            }
        };
    }

    @Override
    public <Resource> WorkflowProcess<Resource> get(final String path,
            final String contentType, final TemplateCategory templateCategory)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException,
            ResourceNotFoundException
    {
        return get(null, path, contentType, templateCategory);
    }

    /**
     * constractor injection
     * 
     * @param stage
     * @param compilerMap
     * @param responseFactory
     * @param environment
     * 
     */
    @Inject
    public TemplateProcessProviderImpl(final Stage stage,
            final EnumMap<TemplateCategory, TemplateCompiler> compilerMap,
            final TemplateResponseFactory responseFactory,
            final WorkflowEnvironment environment)
    {
        this.isDevelopment = stage == Stage.DEVELOPMENT;
        this.compilerMap = compilerMap;
        this.responseFactory = responseFactory;
        this.environment = environment;
    }

    /**
     * logger
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * compiler-map
     */
    private final EnumMap<TemplateCategory, TemplateCompiler> compilerMap;

    /**
     * 環境変数
     */
    private final WorkflowEnvironment environment;

    /**
     * stage
     */
    private final boolean isDevelopment;

    /**
     * responseFactory
     */
    private final TemplateResponseFactory responseFactory;

}
