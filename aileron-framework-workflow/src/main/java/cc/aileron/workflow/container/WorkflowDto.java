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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import cc.aileron.generic.util.Nullable;

/**
 * @author Aileron
 */
public class WorkflowDto implements Nullable
{
    /**
     * nullObject
     */
    static final WorkflowDto nullObject = new WorkflowDto();

    /**
     * execute
     */
    public void execute()
    {
        executor.execute(requestParameters);
    }

    @Override
    public boolean isNull()
    {
        return nullObject.equals(this);
    }

    /**
     * @return リソースクラス
     */
    public Class<?> resourceClass()
    {
        return executor.target();
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this).toString();
    }

    /**
     * @param isThrough
     * @param requestParameters
     * @param executor
     */
    public WorkflowDto(final boolean isThrough,
            final HashMap<String, Object> requestParameters,
            final WorkflowExecutor executor)
    {
        this.isThrough = isThrough;
        this.requestParameters = requestParameters;
        this.executor = executor;
    }

    /**
     * null object constractor
     */
    private WorkflowDto()
    {
        this.executor = null;
        this.requestParameters = null;
        this.isThrough = false;
    }

    /**
     * isThrough
     */
    public final boolean isThrough;

    /**
     * executor
     */
    private final WorkflowExecutor executor;

    /**
     * parameters
     */
    private final HashMap<String, Object> requestParameters;
}
