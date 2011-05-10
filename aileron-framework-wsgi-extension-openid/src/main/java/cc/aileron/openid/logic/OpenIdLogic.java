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
package cc.aileron.openid.logic;

import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.MessageException;

import cc.aileron.openid.model.OpenIdAuthResult;

import com.google.inject.ImplementedBy;

/**
 * @author Aileron
 */
@ImplementedBy(OpenIdLogicImpl.class)
public interface OpenIdLogic
{
    /**
     * @param identifier
     * @return 認証結果
     * @throws DiscoveryException
     * @throws ConsumerException
     * @throws MessageException
     */
    OpenIdAuthResult auth(String identifier) throws DiscoveryException,
            MessageException,
            ConsumerException;

    /**
     * @param returnUrl
     */
    void setReturnUri(String returnUrl);

    /**
     * @param discoveryInformation
     * @return identifier
     * @throws AssociationException
     * @throws DiscoveryException
     * @throws MessageException
     */
    Identifier veryfy(DiscoveryInformation discoveryInformation) throws MessageException,
            DiscoveryException,
            AssociationException;
}
