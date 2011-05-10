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
package cc.aileron.wsgi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Aileron
 * 
 */
class WsgiRequestMock implements HttpServletRequest
{
    @Override
    public Object getAttribute(final String key)
    {
        return attributeMap.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getAttributeNames()
    {
        return null;
    }

    @Override
    public String getAuthType()
    {
        return null;
    }

    @Override
    public String getCharacterEncoding()
    {
        return null;
    }

    @Override
    public int getContentLength()
    {
        return 0;
    }

    @Override
    public String getContentType()
    {
        return null;
    }

    @Override
    public String getContextPath()
    {
        return null;
    }

    @Override
    public Cookie[] getCookies()
    {
        return null;
    }

    @Override
    public long getDateHeader(final String arg0)
    {
        return 0;
    }

    @Override
    public String getHeader(final String arg0)
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getHeaderNames()
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getHeaders(final String arg0)
    {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        return null;
    }

    @Override
    public int getIntHeader(final String arg0)
    {
        return 0;
    }

    @Override
    public String getLocalAddr()
    {
        return null;
    }

    @Override
    public Locale getLocale()
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getLocales()
    {
        return null;
    }

    @Override
    public String getLocalName()
    {

        return null;
    }

    @Override
    public int getLocalPort()
    {

        return 0;
    }

    @Override
    public String getMethod()
    {

        return null;
    }

    @Override
    public String getParameter(final String arg0)
    {

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map getParameterMap()
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getParameterNames()
    {
        return null;
    }

    @Override
    public String[] getParameterValues(final String arg0)
    {

        return null;
    }

    @Override
    public String getPathInfo()
    {

        return null;
    }

    @Override
    public String getPathTranslated()
    {

        return null;
    }

    @Override
    public String getProtocol()
    {

        return null;
    }

    @Override
    public String getQueryString()
    {

        return null;
    }

    @Override
    public BufferedReader getReader() throws IOException
    {

        return null;
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public String getRealPath(final String arg0)
    {
        return null;
    }

    @Override
    public String getRemoteAddr()
    {

        return null;
    }

    @Override
    public String getRemoteHost()
    {

        return null;
    }

    @Override
    public int getRemotePort()
    {

        return 0;
    }

    @Override
    public String getRemoteUser()
    {

        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(final String arg0)
    {

        return null;
    }

    @Override
    public String getRequestedSessionId()
    {

        return null;
    }

    @Override
    public String getRequestURI()
    {

        return null;
    }

    @Override
    public StringBuffer getRequestURL()
    {

        return null;
    }

    @Override
    public String getScheme()
    {

        return null;
    }

    @Override
    public String getServerName()
    {

        return null;
    }

    @Override
    public int getServerPort()
    {

        return 0;
    }

    @Override
    public String getServletPath()
    {

        return null;
    }

    @Override
    public HttpSession getSession()
    {
        return session;
    }

    @Override
    public HttpSession getSession(final boolean arg0)
    {
        return session;
    }

    @Override
    public Principal getUserPrincipal()
    {

        return null;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie()
    {

        return false;
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL()
    {

        return false;
    }

    @Override
    public boolean isRequestedSessionIdValid()
    {

        return false;
    }

    @Override
    public boolean isSecure()
    {

        return false;
    }

    @Override
    public boolean isUserInRole(final String arg0)
    {
        return false;
    }

    @Override
    public void removeAttribute(final String arg0)
    {
    }

    @Override
    public void setAttribute(final String key,
            final Object value)
    {
        attributeMap.put(key, value);
    }

    @Override
    public void setCharacterEncoding(final String arg0) throws UnsupportedEncodingException
    {

    }

    /**
     * @param session
     */
    public WsgiRequestMock(final WsgiSessionMock session)
    {
        this.session = session;
    }

    private final HashMap<String, Object> attributeMap = new HashMap<String, Object>();

    private final WsgiSessionMock session;
}