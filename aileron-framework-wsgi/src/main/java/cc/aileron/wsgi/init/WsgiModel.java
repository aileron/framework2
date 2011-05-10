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
package cc.aileron.wsgi.init;

import java.util.Map.Entry;

import cc.aileron.workflow.WorkflowBinder;
import cc.aileron.workflow.WorkflowConfigure;
import cc.aileron.workflow.WorkflowModule;
import cc.aileron.workflow.container.WorkflowContainer;
import cc.aileron.workflow.environment.WorkflowEnvironment;
import cc.aileron.wsgi.router.WsgiRouter;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Wsgi モジュール郡を扱うモデル層
 * 
 * @author Aileron
 */
public class WsgiModel
{
    /**
     * @param filepath
     * @throws Exception
     */
    public WsgiModel(final String filepath) throws Exception
    {
        final WorkflowEnvironment environment = WorkflowEnvironment.factory.create(filepath);
        final WorkflowModule module = new WsgiBaseModule(environment);
        final WsgiHttpModule httpModule = new WsgiHttpModule();
        final Injector injector = Guice.createInjector(environment.getStage(),
                module,
                httpModule);

        final WorkflowContainer container = injector.getInstance(WorkflowContainer.class);
        final WorkflowBinder binder = injector.getInstance(WorkflowBinder.class);
        final WorkflowConfigure configure = injector.getInstance(environment.getContainerConfigure());
        configure.configure(binder);

        if (environment.routingDebug())
        {
            System.err.println("wsgi url routting configuration --------------------");
            for (final Entry<String, Class<?>> e : container.all().entrySet())
            {
                System.err.println(e.getKey() + "\t" + e.getValue());
            }
        }

        router = injector.getInstance(WsgiRouter.class);
    }

    /**
     * http request から コントローラーを取得する為のルーター
     */
    public final WsgiRouter router;

}
