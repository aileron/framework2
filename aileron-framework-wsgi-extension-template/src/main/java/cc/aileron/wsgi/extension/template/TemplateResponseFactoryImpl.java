/*
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

import static cc.aileron.wsgi.context.WsgiContextProvider.*;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.template.Template;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.wsgi.context.WsgiContext;
import cc.aileron.wsgi.context.WsgiContextProvider;

import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class TemplateResponseFactoryImpl implements TemplateResponseFactory
{
    @Override
    public <Resource> TemplateResponse<Resource> get(final Object global,
            final String contentType, final Charset charset)
    {
        return new TemplateResponse<Resource>()
        {
            @Override
            public void doResponse(final WorkflowActivity<Resource> activity)
                    throws Exception
            {
                final HttpServletRequest request = context().request();
                request.setCharacterEncoding(charset.name());

                final HttpServletResponse response = context().response();
                response.setContentType(contentType);
                // response.setHeader("Pragma", "no-cache");
                // response.setHeader("Cache-Control", "no-cache");
                // response.setDateHeader("Expires", 0);

                final PojoAccessor<?> accessor = getAccessor(global, activity);
                final PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(),
                        charset));
                template.print(writer, accessor);
            }

            @Override
            public void setTemplate(final Template template)
            {
                this.template = template;
            }

            private Template template;
        };
    }

    /**
     * @param activity
     * @return accessor
     */
    PojoAccessor<?> getAccessor(final Object g,
            final WorkflowActivity<?> activity)
    {
        return activity.resourceAccessor().mixin(new Object()
        {
            @SuppressWarnings("unused")
            public final WsgiContext context = WsgiContextProvider.context();

            @SuppressWarnings("unused")
            public final Object global = g;
        });
    }
}
