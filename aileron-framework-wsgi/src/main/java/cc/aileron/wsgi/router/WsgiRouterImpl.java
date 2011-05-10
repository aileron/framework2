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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.workflow.WorkflowMethod;
import cc.aileron.workflow.container.WorkflowContainer;
import cc.aileron.workflow.container.WorkflowDto;
import cc.aileron.workflow.container.WorkflowFindCondition;
import cc.aileron.workflow.environment.WorkflowEnvironment;
import cc.aileron.wsgi.context.WsgiContextImpl;
import cc.aileron.wsgi.context.WsgiContextProvider;
import cc.aileron.wsgi.request.WsgiRequestParameterFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 実装
 * 
 * @author Aileron
 */
@Singleton
public class WsgiRouterImpl implements WsgiRouter
{
    /*
     * (非 Javadoc)
     * 
     * @seecc.aileron.wsgi.router.WsgiRouter#routing(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * javax.servlet.FilterChain)
     */
    @Override
    public void routing(final ServletContext context,
            final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException, FileUploadException
    {
        WsgiContextProvider.context(new WsgiContextImpl(context,
                request,
                response));
        doRouting(request, response, chain);
    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @throws FileUploadException
     */
    private void doRouting(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException, FileUploadException
    {
        final WorkflowFindCondition condition = getFindCondition(request);
        final WorkflowDto dto = container.get(condition);
        if (dto.isNull())
        {
            logger.trace("request is null : {}", condition);
            chain.doFilter(request, response);
            return;
        }

        logger.trace("request is execute : {} {} ",
                dto.resourceClass(),
                condition);
        dto.execute();

        if (dto.isThrough)
        {
            logger.trace("request is through : {} {}",
                    dto.resourceClass(),
                    condition);
            chain.doFilter(request, response);
        }
    }

    /**
     * 
     * @return
     * @throws FileUploadException
     */
    private WorkflowFindCondition getFindCondition(
            final HttpServletRequest request) throws FileUploadException
    {
        final String servletPath = request.getServletPath();
        final String pathInfo = request.getPathInfo() != null ? request.getPathInfo()
                : "";

        final WorkflowFindCondition condition = new WorkflowFindCondition();
        // condition.uri =
        // EncodeConvertorUtils.getConvertor(environment.getEncode())
        // .convert(servletPath + pathInfo);
        condition.uri = servletPath + pathInfo;
        condition.method = WorkflowMethod.valueOf(request.getMethod());
        condition.parameter = requestParameterFactory.create(request);
        return condition;
    }

    /**
     * @param environment
     * @param container
     * @param requestParameterFactory
     */
    @Inject
    public WsgiRouterImpl(final WorkflowEnvironment environment,
            final WorkflowContainer container,
            final WsgiRequestParameterFactory requestParameterFactory)
    {
        this.container = container;
        this.requestParameterFactory = requestParameterFactory;
    }

    private final WorkflowContainer container;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WsgiRequestParameterFactory requestParameterFactory;
}
