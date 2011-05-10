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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

import cc.aileron.generic.util.Cast;
import cc.aileron.workflow.WorkflowJudgment;
import cc.aileron.workflow.WorkflowParameterBinder;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.WorkflowTransition;
import cc.aileron.workflow.container.WorkflowExecutorStatus;
import cc.aileron.workflow.container.binder.WorkflowValidatorAndProcessContainer.WorkflowValidatorAndProcess;

/**
 * @author Aileron
 * @param <T>
 */
public class WorkflowBinderToImpl<T> implements WorkflowBinderTo<T>
{
    @Override
    public WorkflowBinderTo<T> exception(
            final Class<? extends Exception> exception,
            final WorkflowProcess<? super T> process)
    {
        status.exceptionMappings.put(exception,
                Cast.<WorkflowProcess<T>> cast(process));
        return this;
    }

    @Override
    public WorkflowBinderTo<T> exception(
            final Class<? extends Exception> exception,
            final WorkflowTransition transition, final String... value)
    {
        status.exceptionMappings.put(exception,
                Cast.<WorkflowProcess<T>> cast(transitionProcessFactory.create(transition,
                        value)));

        return this;
    }

    @Override
    public WorkflowBinderTo<T> parameterBinder(
            final Class<? extends WorkflowParameterBinder> parameterBinder)
    {
        this.status.parameterBinderContainer.set(parameterBinder);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> parameterBinder(
            final WorkflowParameterBinder parameterBinder)
    {
        this.status.parameterBinderContainer.set(parameterBinder);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(
            final Class<? extends WorkflowJudgment<? super T>> validator,
            final Class<? extends WorkflowProcess<? super T>> process)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(validator,
                process);
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(
            final Class<? extends WorkflowJudgment<? super T>> validator,
            final WorkflowProcess<? super T> process)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(validator,
                process);
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(
            final Class<? extends WorkflowJudgment<? super T>> validator,
            final WorkflowTransition transition, final String... value)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(validator,
                transitionProcessFactory.create(transition, value));
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(
            final Class<? extends WorkflowProcess<? super T>> process)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(process);
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(
            final WorkflowJudgment<? super T> validator,
            final Class<? extends WorkflowProcess<? super T>> process)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(validator,
                process);
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(
            final WorkflowJudgment<? super T> validator,
            final WorkflowProcess<? super T> process)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(validator,
                process);
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(
            final WorkflowJudgment<? super T> validator,
            final WorkflowTransition transition, final String... value)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(validator,
                transitionProcessFactory.create(transition, value));
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(final WorkflowProcess<? super T> process)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(process);
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> process(final WorkflowTransition transition,
            final String... value)
    {
        final WorkflowValidatorAndProcess<T> vp = c.new WorkflowValidatorAndProcess<T>(transitionProcessFactory.create(transition,
                value));
        status.processList.add(vp);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> requestParameterKeys(
            final Class<?> requestParameterInterface)
    {
        return requestParameterKeys(requestParameterInterface, "");
    }

    @Override
    public WorkflowBinderTo<T> requestParameterKeys(
            final Class<?> requestParameterInterface, final String prefix)
    {
        for (final Method method : requestParameterInterface.getMethods())
        {
            if (method.getParameterTypes().length == 0)
            {
                continue;
            }
            final String name = method.getName();
            if (name.equals("wait") || name.equals("equals"))
            {
                continue;
            }
            final String key = prefix + name;
            status.requestParameterKeys.add(key);
        }
        for (final Field f : requestParameterInterface.getFields())
        {
            if (Modifier.isPublic(f.getModifiers())
                    && Modifier.isFinal(f.getModifiers()) == false)
            {
                final String key = prefix + f.getName();
                status.requestParameterKeys.add(key);
            }
        }
        return this;
    }

    @Override
    public WorkflowBinderTo<T> requestParameterKeys(
            final Collection<String> keys)
    {
        status.requestParameterKeys.addAll(keys);
        return this;
    }

    @Override
    public WorkflowBinderTo<T> requestParameterKeys(final String... keys)
    {
        status.requestParameterKeys.addAll(Arrays.asList(keys));
        return this;
    }

    /**
     * @param transitionProcessFactory
     * @param status
     */
    public WorkflowBinderToImpl(
            final WorkflowTransitionProcessFactory transitionProcessFactory,
            final WorkflowExecutorStatus<T> status)
    {
        this.transitionProcessFactory = transitionProcessFactory;
        this.status = status;
        this.c = status.validatorAndProcessContainer;
    }

    /**
     * validator-and-process-container
     */
    private final WorkflowValidatorAndProcessContainer c;

    /**
     * status
     */
    private final WorkflowExecutorStatus<T> status;

    /**
     * transition-process-factory
     */
    private final WorkflowTransitionProcessFactory transitionProcessFactory;
}
