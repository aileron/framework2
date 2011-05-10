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
package cc.aileron.wsgi.init;

import static cc.aileron.generic.util.Cast.*;
import static cc.aileron.wsgi.context.WsgiContextProvider.*;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowModule;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.WorkflowTransition;
import cc.aileron.workflow.container.binder.WorkflowTransitionProcessFactory;
import cc.aileron.workflow.environment.WorkflowEnvironment;
import cc.aileron.workflow.util.WorkflowUriTemplate;
import cc.aileron.wsgi.context.BeforeRequestPath;

/**
 * @author Aileron
 * 
 */
public class WsgiBaseModule extends WorkflowModule
{
    /**
     * @param environment
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    public WsgiBaseModule(final WorkflowEnvironment environment)
            throws IllegalArgumentException, SecurityException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException,
            ClassNotFoundException
    {
        super(environment, WsgiTransitionProcessFactory.class);
    }
}

/**
 * @author Aileron
 */
class WsgiTransitionProcessFactory implements WorkflowTransitionProcessFactory
{
    static interface Provider
    {
        WorkflowProcess<Object> get(final String... args);
    }

    @Override
    public <Resource> WorkflowProcess<Resource> create(
            final WorkflowTransition transition, final String... value)
    {
        switch (transition)
        {
        case NOT_FOUND:
            return cast(notFound);
        case TERM:
            return cast(termination);
        default:
            break;
        }

        if (value.length == 0)
        {
            throw new IllegalArgumentException();
        }

        final WorkflowUriTemplate uriTemplate = new WorkflowUriTemplate(value[0]);

        switch (transition)
        {
        case FORWARD:
            return new WorkflowProcess<Resource>()
            {
                @Override
                public void doProcess(final WorkflowActivity<Resource> activity)
                        throws Exception
                {
                    final HttpServletRequest request = context().request();
                    final HttpServletResponse response = context().response();
                    final ServletContext context = context().application();
                    final String uri = uriTemplate.replace(activity.resourceAccessor());
                    final RequestDispatcher rd = context.getRequestDispatcher(uri);

                    beforeRequestPath.set();

                    rd.forward(request, response);
                }

                @Override
                public String toString()
                {
                    return "forward(" + uriTemplate + ")";
                }

                private final BeforeRequestPath beforeRequestPath = new BeforeRequestPath();
            };

        case LOCALREDIRECT:
            return new WorkflowProcess<Resource>()
            {
                @Override
                public void doProcess(final WorkflowActivity<Resource> activity)
                        throws Exception
                {
                    final String path = context().path()
                            + uriTemplate.replace(activity.resourceAccessor());
                    context().response().sendRedirect(path);
                }

                @Override
                public String toString()
                {
                    return "redirect(" + context().path() + uriTemplate + ")";
                }
            };

        case REDIRECT:
            return new WorkflowProcess<Resource>()
            {
                @Override
                public void doProcess(final WorkflowActivity<Resource> activity)
                        throws Exception
                {
                    final String path = uriTemplate.replace(activity.resourceAccessor());
                    context().response().sendRedirect(path);
                }

                @Override
                public String toString()
                {
                    return "redirect(" + uriTemplate + ")";
                }
            };

        default:
            throw new Error("case式の記述にもれが有ります");
        }
    }

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * notFound
     */
    private final WorkflowProcess<Object> notFound = new WorkflowProcess<Object>()
    {
        @Override
        public void doProcess(final WorkflowActivity<Object> activity)
                throws Exception
        {
            context().response().setStatus(404);
        }

        @Override
        public String toString()
        {
            return "notfound()";
        }
    };

    /**
     * termination
     */
    private final WorkflowProcess<Object> termination = new WorkflowProcess<Object>()
    {
        @Override
        public void doProcess(final WorkflowActivity<Object> activity)
                throws Exception
        {
        }

        @Override
        public String toString()
        {
            return "termination()";
        }
    };
}