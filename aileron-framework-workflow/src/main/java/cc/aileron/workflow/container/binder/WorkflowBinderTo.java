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

import java.util.Collection;

import cc.aileron.workflow.WorkflowJudgment;
import cc.aileron.workflow.WorkflowParameterBinder;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.WorkflowTransition;

/**
 * @author Aileron
 * @param <T>
 */
public interface WorkflowBinderTo<T>
{
    /**
     * @param exception
     * @param process
     * @return this
     */
    WorkflowBinderTo<T> exception(Class<? extends Exception> exception,
            WorkflowProcess<? super T> process);

    /**
     * @param exception
     * @param transition
     * @param value
     * @param process
     * @return this
     */
    WorkflowBinderTo<T> exception(Class<? extends Exception> exception,
            WorkflowTransition transition, String... value);

    /**
     * @param parameterBinder
     * @return this
     */
    WorkflowBinderTo<T> parameterBinder(
            Class<? extends WorkflowParameterBinder> parameterBinder);

    /**
     * @param parameterBinder
     * @return this
     */
    WorkflowBinderTo<T> parameterBinder(WorkflowParameterBinder parameterBinder);

    /**
     * @param validator
     * @param process
     * @return this
     */
    WorkflowBinderTo<T> process(
            Class<? extends WorkflowJudgment<? super T>> validator,
            Class<? extends WorkflowProcess<? super T>> process);

    /**
     * @param validator
     * @param process
     * @return this
     */
    WorkflowBinderTo<T> process(
            Class<? extends WorkflowJudgment<? super T>> validator,
            WorkflowProcess<? super T> process);

    /**
     * @param validator
     * @param transition
     * @param value
     * @return this
     */
    WorkflowBinderTo<T> process(
            Class<? extends WorkflowJudgment<? super T>> validator,
            WorkflowTransition transition, String... value);

    /**
     * @param process
     * @return this
     */
    WorkflowBinderTo<T> process(
            Class<? extends WorkflowProcess<? super T>> process);

    /**
     * @param validator
     * @param process
     * @return this
     */
    WorkflowBinderTo<T> process(WorkflowJudgment<? super T> validator,
            Class<? extends WorkflowProcess<? super T>> process);

    /**
     * @param validator
     * @param process
     * @return this
     */
    WorkflowBinderTo<T> process(WorkflowJudgment<? super T> validator,
            WorkflowProcess<? super T> process);

    /**
     * @param validator
     * @param transition
     * @param value
     * @return this
     */
    WorkflowBinderTo<T> process(WorkflowJudgment<? super T> validator,
            WorkflowTransition transition, String... value);

    /**
     * @param process
     * @return this
     */
    WorkflowBinderTo<T> process(WorkflowProcess<? super T> process);

    /**
     * @param transition
     * @param value
     * @return this
     */
    WorkflowBinderTo<T> process(WorkflowTransition transition, String... value);

    /**
     * @param requestParameterInterface
     * @return this
     */
    WorkflowBinderTo<T> requestParameterKeys(Class<?> requestParameterInterface);

    /**
     * @param requestParameterInterface
     * @param parentKey
     * @return this
     */
    WorkflowBinderTo<T> requestParameterKeys(
            Class<?> requestParameterInterface, String parentKey);

    /**
     * @param keys
     * @return this
     */
    WorkflowBinderTo<T> requestParameterKeys(Collection<String> keys);

    /**
     * @param keys
     * @return this
     */
    WorkflowBinderTo<T> requestParameterKeys(String... keys);
}
