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
package cc.aileron.workflow.container;

import java.util.HashMap;
import java.util.List;

import cc.aileron.generic.util.Cast;
import cc.aileron.generic.util.SkipList;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.WorkflowTransition;
import cc.aileron.workflow.container.binder.WorkflowParameterBinderContainer;
import cc.aileron.workflow.container.binder.WorkflowValidatorAndProcessContainer;
import cc.aileron.workflow.exception.NotFoundException;

/**
 * @author Aileron
 * @param <T>
 */
public class WorkflowExecutorStatus<T>
{
    /**
     * @return keys
     */
    public List<String> keys()
    {
        final List<String> keys = new SkipList<String>();
        keys.addAll(requestParameterKeys);
        keys.addAll(uriParameterKeys);
        return keys;
    }

    /**
     * @param parameterBinderContainer
     * @param validatorAndProcessContainer
     */
    public WorkflowExecutorStatus(
            final WorkflowParameterBinderContainer parameterBinderContainer,
            final WorkflowValidatorAndProcessContainer validatorAndProcessContainer)
    {
        this.parameterBinderContainer = parameterBinderContainer;
        this.validatorAndProcessContainer = validatorAndProcessContainer;

        try
        {
            exceptionMappings.put(NotFoundException.class,
                    Cast.<WorkflowProcess<T>> cast(validatorAndProcessContainer.transitionProcessFactory.create(WorkflowTransition.NOT_FOUND)));
        }
        catch (final UnsupportedOperationException e)
        {
        }
    }

    /**
     * exceptionMappings
     */
    public final HashMap<Class<? extends Exception>, WorkflowProcess<T>> exceptionMappings = new HashMap<Class<? extends Exception>, WorkflowProcess<T>>();

    /**
     * workflow-parameter-binder
     */
    public final WorkflowParameterBinderContainer parameterBinderContainer;

    /**
     * processList
     */
    public final List<WorkflowValidatorAndProcessContainer.WorkflowValidatorAndProcess<T>> processList = new SkipList<WorkflowValidatorAndProcessContainer.WorkflowValidatorAndProcess<T>>();

    /**
     * requestParameterKeys
     */
    public final List<String> requestParameterKeys = new SkipList<String>();

    /**
     * uri-parameter-keys
     */
    public final List<String> uriParameterKeys = new SkipList<String>();

    /**
     * validator-and-process-container
     */
    public final WorkflowValidatorAndProcessContainer validatorAndProcessContainer;
}
