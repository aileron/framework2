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
package cc.aileron.wsgi.test;

import javax.servlet.ServletContext;

import cc.aileron.workflow.WorkflowModule;
import cc.aileron.workflow.environment.WorkflowEnvironment;
import cc.aileron.wsgi.context.WsgiContextImpl;
import cc.aileron.wsgi.context.WsgiContextProvider;
import cc.aileron.wsgi.init.WsgiBaseModule;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * @author Aileron
 */
public class WsgiTryModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.install(module);
    }

    /**
     * @param filepath
     * @throws Exception
     */
    public WsgiTryModule(final String filepath) throws Exception
    {
        final WorkflowEnvironment environment = WorkflowEnvironment.factory.create(filepath);
        try
        {
            module = new WsgiBaseModule(environment);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        final ServletContext context = null;
        final WsgiSessionMock session = new WsgiSessionMock();
        final WsgiRequestMock request = new WsgiRequestMock(session);
        final WsgiResponseMock response = new WsgiResponseMock();
        WsgiContextProvider.context(new WsgiContextImpl(context,
                request,
                response));
    }

    private final WorkflowModule module;
}