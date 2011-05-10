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
package cc.aileron.workflow;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cc.aileron.workflow.container.WorkflowContext;
import cc.aileron.workflow.container.binder.WorkflowTransitionProcessFactory;
import cc.aileron.workflow.environment.WorkflowEnvironment;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * @author Aileron
 */
public class WorkflowModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.install(applicationModule);
        binder.bind(WorkflowEnvironment.class).toInstance(environment);
        binder.bind(WorkflowTransitionProcessFactory.class)
                .to(transitionProcessFactory)
                .asEagerSingleton();
        binder.bind(WorkflowParameter.class)
                .toProvider(new Provider<WorkflowParameter>()
                {
                    @Override
                    public WorkflowParameter get()
                    {
                        return WorkflowContext.parameters.get();
                    }
                });
    }

    private Module getApplicationModule(
            final Class<? extends Module> appMobuleClass,
            final WorkflowEnvironment environment)
            throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            SecurityException, NoSuchMethodException
    {
        /*
         * final WorkflowEnvironment を引数に取るコンストラクタによるインスタンス化
         */
        Constructor<? extends Module> envConstractor = null;
        try
        {
            envConstractor = appMobuleClass.getConstructor(WorkflowEnvironment.class);
        }
        catch (final Exception e)
        {
        }
        if (envConstractor != null)
        {
            return envConstractor.newInstance(environment);
        }

        /*
         * default constractor によるインスタンス化
         */
        return appMobuleClass.getConstructor().newInstance();
    }

    /**
     * @param environment
     * @param transitionProcessFactory
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    public WorkflowModule(
            final WorkflowEnvironment environment,
            final Class<? extends WorkflowTransitionProcessFactory> transitionProcessFactory)
            throws IllegalArgumentException, SecurityException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException,
            ClassNotFoundException
    {
        this.environment = environment;
        this.transitionProcessFactory = transitionProcessFactory;
        this.applicationModule = getApplicationModule(environment.getApplicationModule(),
                environment);
    }

    private final Module applicationModule;
    private final WorkflowEnvironment environment;
    private final Class<? extends WorkflowTransitionProcessFactory> transitionProcessFactory;
}
