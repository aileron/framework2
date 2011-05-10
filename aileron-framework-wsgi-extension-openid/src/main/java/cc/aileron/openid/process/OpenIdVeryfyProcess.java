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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.openid.logic.OpenIdLogic;
import cc.aileron.openid.model.OpenIdTransitionUris;
import cc.aileron.openid.resource.OpenIdResource;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class OpenIdVeryfyProcess implements WorkflowProcess<OpenIdResource>
{
    @Override
    public void doProcess(final WorkflowActivity<OpenIdResource> activity) throws Exception
    {
        final OpenIdResource resource = activity.resource();
        try
        {
            resource.userIdentifier = logic.veryfy(resource.discovered);
            resource.transitionUrl = uris.success;
        }
        catch (final Exception e)
        {
            logger.error("open-id-veryfy-error", e);
            resource.transitionUrl = uris.failed;
        }
    }

    /**
     * @param logic
     * @param uris
     */
    @Inject
    public OpenIdVeryfyProcess(
        final OpenIdLogic logic,
        final OpenIdTransitionUris uris)
    {
        this.logic = logic;
        this.uris = uris;
    }

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * uris
     */
    private final OpenIdTransitionUris uris;

    /**
     * logic
     */
    private final OpenIdLogic logic;
}
