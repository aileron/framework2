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
package cc.aileron.wsgi.ext;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class ExecuteProcess implements WorkflowProcess<ExecutableResource>
{
    @Override
    public void doProcess(final WorkflowActivity<ExecutableResource> activity) throws Exception
    {
        activity.resource()
            .execute();
    }
}
