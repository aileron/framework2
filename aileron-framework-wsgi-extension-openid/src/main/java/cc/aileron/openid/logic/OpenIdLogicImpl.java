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

import static cc.aileron.wsgi.context.WsgiContextProvider.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;

import cc.aileron.generic.util.Cast;
import cc.aileron.openid.model.OpenIdAuthResult;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class OpenIdLogicImpl implements OpenIdLogic
{
    @Override
    public OpenIdAuthResult auth(final String identifier)
            throws DiscoveryException, MessageException, ConsumerException
    {
        final List<DiscoveryInformation> discoveries = Cast.cast(consumerManager.discover(identifier));
        final DiscoveryInformation discovered = consumerManager.associate(discoveries);
        final AuthRequest authRequest = consumerManager.authenticate(discovered,
                context().path() + returnUrl);
        return new OpenIdAuthResult(discovered, authRequest);
    }

    @Override
    public void setReturnUri(final String returnUrl)
    {
        this.returnUrl = returnUrl.substring(1);
    }

    @Override
    public Identifier veryfy(final DiscoveryInformation discovered)
            throws MessageException, DiscoveryException, AssociationException
    {
        final ParameterList openidResp = new ParameterList(context().request()
                .getParameterMap());

        final String receivingURL = getReceivingURL();

        final VerificationResult verification = consumerManager.verify(receivingURL,
                openidResp,
                discovered);

        return verification.getVerifiedId();
    }

    /**
     * @return receivingURL
     */
    private String getReceivingURL()
    {
        final HttpServletRequest request = context().request();
        final String queryString = request.getQueryString();
        final StringBuffer receivingURL = request.getRequestURL();
        if (queryString != null && queryString.length() > 0)
        {
            receivingURL.append("?").append(queryString);
        }
        return receivingURL.toString();
    }

    /**
     * @param consumerManager
     */
    @Inject
    public OpenIdLogicImpl(final ConsumerManager consumerManager)
    {
        this.consumerManager = consumerManager;

    }

    /**
     * ConsumerManager
     */
    private final ConsumerManager consumerManager;

    /**
     * returnUrl
     */
    private String returnUrl;
}
