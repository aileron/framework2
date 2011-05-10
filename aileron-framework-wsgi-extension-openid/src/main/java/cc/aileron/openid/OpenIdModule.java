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

import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;

import cc.aileron.openid.model.OpenIdTransitionUris;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * open-id-module
 * 
 * @author Aileron
 */
public class OpenIdModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.bind(ConsumerManager.class)
            .toInstance(consumerManager);
        binder.bind(OpenIdTransitionUris.class)
            .toInstance(uri);
        binder.bind(OpenIdSetting.class)
            .toInstance(setting);
    }

    /**
     * @param setting
     * @throws ConsumerException
     */
    public OpenIdModule(final OpenIdSetting setting) throws ConsumerException
    {
        this.setting = setting;
        this.uri = new OpenIdTransitionUris();
        this.consumerManager = new ConsumerManager();
    }

    /**
     * consumerManager
     */
    private final ConsumerManager consumerManager;

    /**
     * setting
     */
    private final OpenIdSetting setting;

    /**
     * uri
     */
    private final OpenIdTransitionUris uri;
}
