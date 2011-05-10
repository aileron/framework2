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
package cc.aileron.workflow.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cc.aileron.workflow.container.tree.WorkflowTreeContainer;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 実装
 * 
 * @author Aileron
 */
@Singleton
public class WorkflowContainerImpl implements WorkflowContainer
{
    @Override
    public Map<String, Class<?>> all()
    {
        final HashMap<String, Class<?>> result = new HashMap<String, Class<?>>();
        for (final Entry<String, Integer> e : container.all().entrySet())
        {
            result.put(e.getKey(), executors.get(e.getValue()).target());
        }
        return result;
    }

    @Override
    public WorkflowDto get(final WorkflowFindCondition c)
    {
        final HashMap<String, Object> uriparameters = new HashMap<String, Object>();
        final int id = container.get(c.uri,
                c.method,
                uriparameters,
                c.parameter.getKeys());
        if (id == 0)
        {
            return WorkflowDto.nullObject;
        }

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        for (final String key : c.parameter.getKeys())
        {
            parameters.put(key, c.parameter.get(key));
        }
        parameters.putAll(uriparameters);

        return new WorkflowDto(conditions.get(id).isThrough,
                parameters,
                executors.get(id));
    }

    @Override
    public void put(final WorkflowRegistryCondition c,
            final WorkflowExecutor executor)
    {
        final int id = idx++;
        container.put(c.uri, c.method, c.overrideKey, id);
        executors.put(id, executor);
        conditions.put(id, c);
    }

    /**
     * constractor injection
     * 
     * @param container
     */
    @Inject
    public WorkflowContainerImpl(final WorkflowTreeContainer container)
    {
        this.container = container;
    }

    /**
     * thmap
     */
    private final Map<Integer, WorkflowRegistryCondition> conditions = new HashMap<Integer, WorkflowRegistryCondition>();

    /**
     * container
     */
    private final WorkflowTreeContainer container;

    /**
     * map
     */
    private final Map<Integer, WorkflowExecutor> executors = new HashMap<Integer, WorkflowExecutor>();

    /**
     * idx
     */
    private int idx = 1;
}
