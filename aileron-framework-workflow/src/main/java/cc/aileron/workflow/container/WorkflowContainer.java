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

import java.util.Map;

import com.google.inject.ImplementedBy;

/**
 * リクエストに一意なキーを割りふる為のコンテナ
 * 
 * @author Aileron
 */
@ImplementedBy(WorkflowContainerImpl.class)
public interface WorkflowContainer
{
    /**
     * @return all
     */
    Map<String, Class<?>> all();

    /**
     * @param condition
     * @param controller
     * @return dto is not null
     */
    WorkflowDto get(WorkflowFindCondition condition);

    /**
     * @param condition
     * @param executor
     */
    void put(WorkflowRegistryCondition condition, WorkflowExecutor executor);
}
