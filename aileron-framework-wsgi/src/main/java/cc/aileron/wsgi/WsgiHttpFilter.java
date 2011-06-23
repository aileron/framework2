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
package cc.aileron.wsgi;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.vfs.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.commons.resource.Resource;
import cc.aileron.commons.resource.ResourceConvertUtils;
import cc.aileron.commons.resource.ResourceLoaders;
import cc.aileron.commons.resource.ResourceLoaders.Type;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.wsgi.context.WsgiContextProvider;
import cc.aileron.wsgi.init.WsgiModel;

/**
 * @author Aileron
 */
public class WsgiHttpFilter implements Filter
{
    @Override
    public void destroy()
    {
        WsgiContextProvider.context(null);
    }

    @Override
    public void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException
    {
        try
        {
            model.router.routing(context,
                    (HttpServletRequest) request,
                    (HttpServletResponse) response,
                    chain);
        }
        catch (final FileUploadException e)
        {
            throw new ServletException(e);
        }
    }

    @Override
    public void init(final FilterConfig config) throws ServletException
    {
        context = config.getServletContext();
        try
        {
            appendResourceLoader(context.getRealPath("/"));
            model = new WsgiModel(config.getInitParameter("environment"));
        }
        catch (final Exception e)
        {
            logger.error("init-error", e);
            throw new ServletException(e);
        }
    }

    /**
     * @param path
     * @throws ResourceNotFoundException
     * @throws FileSystemException
     * @throws ServletException
     */
    private void appendResourceLoader(final String path)
            throws FileSystemException, ResourceNotFoundException
    {
        final Resource resource = ResourceConvertUtils.convertFile(new File(path));
        ResourceLoaders.appendResourceLoader(Type.APPLICATIONPATH, resource);
    }

    /**
     */
    public WsgiHttpFilter()
    {
    }

    /**
     * @param model
     */
    public WsgiHttpFilter(final WsgiModel model)
    {
        this.model = model;
    }

    /**
     * context
     */
    private ServletContext context;

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * model
     */
    private WsgiModel model;
}