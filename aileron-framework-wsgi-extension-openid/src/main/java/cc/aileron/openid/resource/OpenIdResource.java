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
package cc.aileron.openid.resource;

import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;

import cc.aileron.openid.OpenIdSetting;
import cc.aileron.wsgi.WsgiSessionScoped;

import com.google.inject.Inject;

/**
 * @author Aileron
 */
@WsgiSessionScoped
public class OpenIdResource
{
    /**
     * @param setting
     */
    @Inject
    public OpenIdResource(final OpenIdSetting setting)
    {
        requestIdentifier = setting.requestIdentifier;
    }

    /**
     * discovery-information
     */
    public DiscoveryInformation discovered;

    /**
     * request-openid-identifier
     */
    public String requestIdentifier;

    /**
     * transition url
     */
    public String transitionUrl;

    /**
     * user-identifier
     */
    public Identifier userIdentifier;
}
