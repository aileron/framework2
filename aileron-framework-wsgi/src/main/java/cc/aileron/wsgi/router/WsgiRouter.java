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
package cc.aileron.wsgi.router;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import com.google.inject.ImplementedBy;

/**
 * wsgi-router
 * 
 * @author Aileron
 */
@ImplementedBy(WsgiRouterImpl.class)
public interface WsgiRouter
{
    /**
     * @param context
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     * @throws FileUploadException
     */
    void routing(ServletContext context,
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException,
            ServletException,
            FileUploadException;
}
