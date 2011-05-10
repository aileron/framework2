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
package cc.aileron.workflow.activity;

import java.util.Map;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.workflow.WorkflowActivity;

/**
 * @author Aileron
 * @param <Resource>
 */
class WorkflowActivityImpl<Resource> implements WorkflowActivity<Resource>
{
    @Override
    public Map<String, Object> paramters()
    {
        return parameters;
    }

    @Override
    public Resource resource()
    {
        return resourceAccessor.toTarget();
    }

    @Override
    public PojoAccessor<Resource> resourceAccessor()
    {
        return resourceAccessor;
    }

    /**
     * @param parameters
     * @param resourceAccessor
     */
    public WorkflowActivityImpl(final Map<String, Object> parameters,
            final PojoAccessor<Resource> resourceAccessor)
    {
        this.parameters = parameters;
        this.resourceAccessor = resourceAccessor;
    }

    private final Map<String, Object> parameters;
    private final PojoAccessor<Resource> resourceAccessor;
}
