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
package cc.aileron.wsgi.context;

import static cc.aileron.wsgi.context.WsgiContextProvider.*;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class BeforeRequestPath
{
    /**
     * @return BeforeRequestPath
     */
    public String get()
    {
        final Object r = context().request().getAttribute(KEY);
        return r == null ? null : r.toString();
    }

    /**
     * set
     */
    public void set()
    {
        final HttpServletRequest request = context().request();
        final String path = request.getServletPath();
        request.setAttribute(KEY, path);
    }

    /**
     * KEY
     */
    private final String KEY = this.getClass().getName();
}
