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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aileron
 * 
 */
class WsgiResponseMock implements HttpServletResponse
{

    @Override
    public void addCookie(final Cookie arg0)
    {

    }

    @Override
    public void addDateHeader(final String arg0,
            final long arg1)
    {

    }

    @Override
    public void addHeader(final String arg0,
            final String arg1)
    {

    }

    @Override
    public void addIntHeader(final String arg0,
            final int arg1)
    {

    }

    @Override
    public boolean containsHeader(final String arg0)
    {

        return false;
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public String encodeRedirectUrl(final String arg0)
    {

        return null;
    }

    @Override
    public String encodeRedirectURL(final String arg0)
    {

        return null;
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public String encodeUrl(final String arg0)
    {

        return null;
    }

    @Override
    public String encodeURL(final String arg0)
    {

        return null;
    }

    @Override
    public void flushBuffer() throws IOException
    {

    }

    @Override
    public int getBufferSize()
    {

        return 0;
    }

    @Override
    public String getCharacterEncoding()
    {

        return null;
    }

    @Override
    public String getContentType()
    {

        return null;
    }

    @Override
    public Locale getLocale()
    {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException
    {
        return new PrintWriter(System.out);
    }

    @Override
    public boolean isCommitted()
    {

        return false;
    }

    @Override
    public void reset()
    {

    }

    @Override
    public void resetBuffer()
    {

    }

    @Override
    public void sendError(final int arg0) throws IOException
    {

    }

    @Override
    public void sendError(final int arg0,
            final String arg1) throws IOException
    {

    }

    @Override
    public void sendRedirect(final String arg0) throws IOException
    {

    }

    @Override
    public void setBufferSize(final int arg0)
    {

    }

    @Override
    public void setCharacterEncoding(final String arg0)
    {

    }

    @Override
    public void setContentLength(final int arg0)
    {

    }

    @Override
    public void setContentType(final String arg0)
    {

    }

    @Override
    public void setDateHeader(final String arg0,
            final long arg1)
    {

    }

    @Override
    public void setHeader(final String arg0,
            final String arg1)
    {

    }

    @Override
    public void setIntHeader(final String arg0,
            final int arg1)
    {

    }

    @Override
    public void setLocale(final Locale arg0)
    {

    }

    @Override
    public void setStatus(final int arg0)
    {

    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public void setStatus(final int arg0,
            final String arg1)
    {

    }

}
