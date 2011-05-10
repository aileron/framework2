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
package cc.aileron.openid.process;

import cc.aileron.openid.logic.OpenIdLogic;
import cc.aileron.openid.model.OpenIdAuthResult;
import cc.aileron.openid.resource.OpenIdResource;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class OpenIdAuthProcess implements WorkflowProcess<OpenIdResource>
{
    /*
     * (非 Javadoc)
     * 
     * @see
     * cc.aileron.job.WorkflowProcess#doProcess(cc.aileron.job.WorkflowActivity)
     */
    @Override
    public void doProcess(final WorkflowActivity<OpenIdResource> activity) throws Exception
    {
        final OpenIdResource resource = activity.resource();
        final OpenIdAuthResult result = logic.auth(resource.requestIdentifier);

        resource.discovered = result.discoveryInformation;
        resource.transitionUrl = result.authRequest.getDestinationUrl(true);
    }

    /**
     * @param logic
     */
    @Inject
    public OpenIdAuthProcess(final OpenIdLogic logic)
    {
        this.logic = logic;
    }

    private final OpenIdLogic logic;
}
