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
package cc.aileron.workflow.container.binder;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.workflow.WorkflowBinder;
import cc.aileron.workflow.WorkflowConfigure;
import cc.aileron.workflow.WorkflowMethod;
import cc.aileron.workflow.activity.WorkflowActivityFactory;
import cc.aileron.workflow.container.WorkflowContainer;
import cc.aileron.workflow.container.WorkflowExecutorImpl;
import cc.aileron.workflow.container.WorkflowExecutorStatus;
import cc.aileron.workflow.container.WorkflowRegistryCondition;
import cc.aileron.workflow.util.WorkflowUriTemplate;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class WorkflowBinderImpl implements WorkflowBinder
{
    @Override
    public <T> WorkflowBinderBind<T> bind(final Class<T> target)
    {
        final Provider<PojoAccessor<T>> provider = new Provider<PojoAccessor<T>>()
        {
            @Override
            public PojoAccessor<T> get()
            {
                return accessorManager.from(target);
            }
        };
        final WorkflowExecutorStatus<T> status = new WorkflowExecutorStatus<T>(parameterBinderContainer,
                validatorAndProcessContainer);
        final WorkflowRegistryCondition condition = new WorkflowRegistryCondition();
        return new WorkflowBinderBind<T>()
        {

            @Override
            public WorkflowBinderBind<T> isThrough(final boolean isThrough)
            {
                condition.isThrough = isThrough;
                return this;
            }

            @Override
            public WorkflowBinderBind<T> method(final WorkflowMethod method)
            {
                condition.method = method;
                return this;
            }

            @Override
            public WorkflowBinderBind<T> overrideKey(final String overrideKey)
            {
                condition.overrideKey = overrideKey;
                return this;
            }

            @Override
            public WorkflowBinderTo<T> to()
            {
                final WorkflowBinderTo<T> to = new WorkflowBinderToImpl<T>(processFactory,
                        status);
                final WorkflowExecutorImpl<T> executor = new WorkflowExecutorImpl<T>(condition,
                        target,
                        activityFactory,
                        provider,
                        status);
                container.put(condition, executor);
                return to;
            }

            @Override
            public WorkflowBinderBind<T> uri(final String uri)
            {
                status.uriParameterKeys.clear();
                status.uriParameterKeys.addAll(new WorkflowUriTemplate(uri).extract());
                condition.uri = uri;
                return this;
            }
        };
    }

    @Override
    public void install(final WorkflowConfigure configure) throws Exception
    {
        configure.configure(this);
    }

    /**
     * @param processFactory
     * @param activityFactory
     * @param container
     * @param accessorManager
     * @param parameterBinderContainer
     * @param validatorAndProcessContainer
     */
    @Inject
    public WorkflowBinderImpl(
            final WorkflowTransitionProcessFactory processFactory,
            final WorkflowActivityFactory activityFactory,
            final WorkflowContainer container,
            final PojoAccessorManager accessorManager,
            final WorkflowParameterBinderContainer parameterBinderContainer,
            final WorkflowValidatorAndProcessContainer validatorAndProcessContainer)
    {
        this.processFactory = processFactory;
        this.activityFactory = activityFactory;
        this.container = container;
        this.accessorManager = accessorManager;
        this.parameterBinderContainer = parameterBinderContainer;
        this.validatorAndProcessContainer = validatorAndProcessContainer;
    }

    /**
     * accessorFactory
     */
    final PojoAccessorManager accessorManager;

    final WorkflowActivityFactory activityFactory;

    /**
     * container
     */
    final WorkflowContainer container;

    /**
     * parameter-binder-container
     */
    final WorkflowParameterBinderContainer parameterBinderContainer;

    /**
     * transition-process-factory
     */
    final WorkflowTransitionProcessFactory processFactory;

    /**
     * validator-and-process-container
     */
    final WorkflowValidatorAndProcessContainer validatorAndProcessContainer;
}
