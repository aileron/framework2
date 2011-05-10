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
package cc.aileron.openid;

import static cc.aileron.workflow.WorkflowMethod.*;
import static cc.aileron.workflow.WorkflowTransition.*;
import cc.aileron.openid.logic.OpenIdLogic;
import cc.aileron.openid.model.OpenIdTransitionUris;
import cc.aileron.openid.process.OpenIdAuthProcess;
import cc.aileron.openid.process.OpenIdAuthValidator;
import cc.aileron.openid.process.OpenIdVeryfyProcess;
import cc.aileron.openid.resource.OpenIdResource;
import cc.aileron.workflow.WorkflowBinder;
import cc.aileron.workflow.WorkflowConfigure;
import cc.aileron.workflow.container.binder.WorkflowBinderTo;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class OpenIdConfigure implements WorkflowConfigure
{
    /**
     * RequestIdentifier
     */
    private static final String RequestIdentifier = "requestIdentifier";

    /*
     * (非 Javadoc)
     * 
     * @see
     * cc.aileron.wsgi.container.WsgiContainerConfigure#configure(cc.aileron
     * .wsgi.container.WsgiContainer)
     */
    @Override
    public void configure(final WorkflowBinder binder) throws Exception
    {
        authBind(binder.bind(OpenIdResource.class)
            .uri(auth)
            .method(GET)
            .to());

        authBind(binder.bind(OpenIdResource.class)
            .uri(auth)
            .method(POST)
            .to());

        binder.bind(OpenIdResource.class)
            .uri(veryfy)
            .method(GET)
            .to()
            .process(OpenIdVeryfyProcess.class)
            .process(LOCALREDIRECT, "${transitionUrl}");

        binder.bind(OpenIdResource.class)
            .uri(veryfy)
            .method(POST)
            .to()
            .process(OpenIdVeryfyProcess.class)
            .process(LOCALREDIRECT, "${transitionUrl}");
    }

    /**
     * @param auth
     */
    public void uriAuth(final String auth)
    {
        this.auth = auth;
    }

    /**
     * @param failed
     */
    public void uriFailed(final String failed)
    {
        this.uris.failed = failed;
    }

    /**
     * @param success
     */
    public void uriSuccess(final String success)
    {
        this.uris.success = success;
    }

    /**
     * @param veryfy
     */
    public void uriVeryfy(final String veryfy)
    {
        this.veryfy = veryfy;
        logic.setReturnUri(veryfy);
    }

    /**
     * @param to
     */
    private void authBind(final WorkflowBinderTo<OpenIdResource> to)
    {
        if (setting.isRequestIdentifier())
        {
            to.requestParameterKeys(RequestIdentifier);
        }

        to.process(OpenIdAuthValidator.class, LOCALREDIRECT, this.uris.failed)
            .process(OpenIdAuthProcess.class)
            .process(REDIRECT, "${transitionUrl}");
    }

    /**
     * @param uris
     * @param logic
     * @param setting
     */
    @Inject
    public OpenIdConfigure(
        final OpenIdTransitionUris uris,
        final OpenIdLogic logic,
        final OpenIdSetting setting)
    {
        this.uris = uris;
        this.logic = logic;
        this.setting = setting;
    }

    /**
     * auth
     */
    private String auth;

    /**
     * logic
     */
    private final OpenIdLogic logic;

    /**
     * setting
     */
    private final OpenIdSetting setting;

    /**
     * uris
     */
    private final OpenIdTransitionUris uris;

    /**
     * veryfy
     */
    private String veryfy;
}
