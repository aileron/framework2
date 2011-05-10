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
package cc.aileron.wsgi.context;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.aileron.workflow.WorkflowParameter;
import cc.aileron.workflow.container.WorkflowContext;

/**
 * @author Aileron
 */
public class WsgiContextImpl implements WsgiContext
{
    @Override
    public ServletContext application()
    {
        return context;
    }

    @Override
    public WsgiRequestAttribute attribute()
    {
        return attribute;
    }

    @Override
    public WorkflowParameter parameters()
    {
        return WorkflowContext.parameters.get();
    }

    @Override
    public String path()
    {
        final HttpServletRequest r = request;
        final StringBuilder b = new StringBuilder();
        b.append(r.getScheme());
        b.append("://");
        b.append(r.getServerName());
        if (r.getServerPort() != 80)
        {
            b.append(":");
            b.append(r.getServerPort());
        }
        b.append(r.getContextPath());
        return b.toString();
    }

    @Override
    public HttpServletRequest request()
    {
        return request;
    }

    @Override
    public HttpServletResponse response()
    {
        return response;
    }

    /**
     * @param context
     * @param request
     * @param response
     */
    public WsgiContextImpl(final ServletContext context,
            final HttpServletRequest request, final HttpServletResponse response)
    {
        this.context = context;
        this.request = request;
        this.response = response;
        this.attribute = new WsgiRequestAttribute()
        {
            @Override
            public Object get(final String name)
            {
                return request.getAttribute(name);
            }
        };
    }

    private final WsgiRequestAttribute attribute;
    private final ServletContext context;
    private final HttpServletRequest request;

    private final HttpServletResponse response;
}
