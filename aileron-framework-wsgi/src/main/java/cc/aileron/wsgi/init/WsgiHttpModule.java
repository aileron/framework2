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
package cc.aileron.wsgi.init;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.aileron.generic.EString;
import cc.aileron.wsgi.WsgiRequestHeader;
import cc.aileron.wsgi.WsgiRequestScoped;
import cc.aileron.wsgi.WsgiSessionScoped;
import cc.aileron.wsgi.context.WsgiContext;
import cc.aileron.wsgi.context.WsgiContextProvider;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * @author Aileron
 */
public class WsgiHttpModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        final RequestScope requestScope = new RequestScope();
        final SessionScope sessionScope = new SessionScope();

        binder.bindScope(WsgiRequestScoped.class, requestScope);
        binder.bindScope(WsgiSessionScoped.class, sessionScope);

        final Provider<HttpServletRequest> requestProvider = new RequestProvider();
        final Provider<HttpServletResponse> responseProvider = new ResponseProvider();

        binder.bind(HttpServletRequest.class).toProvider(requestProvider);
        binder.bind(HttpServletResponse.class).toProvider(responseProvider);
        binder.bind(ServletRequest.class).toProvider(requestProvider);
        binder.bind(ServletResponse.class).toProvider(responseProvider);

        binder.bind(WsgiRequestHeader.class).toInstance(new WsgiRequestHeader()
        {
            @Override
            public EString get(final String key)
            {
                return new EString(WsgiContextProvider.context()
                        .request()
                        .getHeader(key));
            }
        });

        binder.bind(WsgiContext.class).toProvider(new Provider<WsgiContext>()
        {
            @Override
            public WsgiContext get()
            {
                return WsgiContextProvider.context();
            }
        });
    }
}

/**
 * 
 * @author Aileron
 * 
 */
class RequestProvider implements Provider<HttpServletRequest>
{
    @Override
    public HttpServletRequest get()
    {
        return WsgiContextProvider.context().request();
    }
}

/**
 * 
 * @author Aileron
 * 
 */
class RequestScope implements Scope
{

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator)
    {
        final String name = key.toString();
        return new Provider<T>()
        {
            @Override
            public T get()
            {
                final HttpServletRequest request = WsgiContextProvider.context()
                        .request();
                synchronized (request)
                {
                    @SuppressWarnings("unchecked")
                    T t = (T) request.getAttribute(name);
                    if (t == null)
                    {
                        t = creator.get();
                        request.setAttribute(name, t);
                    }
                    return t;
                }
            }
        };
    }
}

/**
 * 
 * @author Aileron
 * 
 */
class ResponseProvider implements Provider<HttpServletResponse>
{
    @Override
    public HttpServletResponse get()
    {
        return WsgiContextProvider.context().response();
    }
}

/**
 * 
 * @author Aileron
 * 
 */
class SessionScope implements Scope
{
    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator)
    {
        final String name = key.toString();
        return new Provider<T>()
        {
            @Override
            public T get()
            {
                final HttpSession session = WsgiContextProvider.context()
                        .request()
                        .getSession();
                synchronized (session)
                {
                    @SuppressWarnings("unchecked")
                    T t = (T) session.getAttribute(name);
                    if (t == null)
                    {
                        t = creator.get();
                        session.setAttribute(name, t);
                    }
                    return t;
                }
            }
        };
    }
}