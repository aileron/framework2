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

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author Aileron
 * 
 */
@SuppressWarnings("deprecation")
class WsgiSessionMock implements HttpSession
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
    public long getCreationTime()
    {

        return 0;
    }

    @Override
    public String getId()
    {

        return null;
    }

    @Override
    public long getLastAccessedTime()
    {

        return 0;
    }

    @Override
    public int getMaxInactiveInterval()
    {

        return 0;
    }

    @Override
    public ServletContext getServletContext()
    {

        return null;
    }

    @Override
    public HttpSessionContext getSessionContext()
    {
        return null;
    }

    @Override
    public Object getValue(final String arg0)
    {

        return null;
    }

    @Override
    public String[] getValueNames()
    {

        return null;
    }

    @Override
    public void invalidate()
    {

    }

    @Override
    public boolean isNew()
    {

        return false;
    }

    @Override
    public void putValue(final String arg0,
            final Object arg1)
    {

    }

    @Override
    public void removeAttribute(final String arg0)
    {

    }

    @Override
    public void removeValue(final String arg0)
    {

    }

    @Override
    public void setAttribute(final String key,
            final Object value)
    {
        attributeMap.put(key,
                value);

    }

    @Override
    public void setMaxInactiveInterval(final int arg0)
    {

    }

    private final HashMap<String, Object> attributeMap = new HashMap<String, Object>();
}