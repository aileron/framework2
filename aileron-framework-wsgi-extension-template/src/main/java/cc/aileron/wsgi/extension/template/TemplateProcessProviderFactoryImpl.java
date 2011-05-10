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
package cc.aileron.wsgi.extension.template;

import static cc.aileron.generic.util.StringConvertor.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import cc.aileron.generic.util.Cast;
import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class TemplateProcessProviderFactoryImpl implements
        TemplateProcessProviderFactory
{
    @Override
    public <P> P get(final Class<P> target)
    {
        final TemplateProcessAnnotation annotation = target.getAnnotation(TemplateProcessAnnotation.class);
        if (annotation == null)
        {
            throw new NullPointerException("TemplateProcessAnnotation is null");
        }
        return Cast.<P> cast(Proxy.newProxyInstance(this.getClass()
                .getClassLoader(),
                new Class[] { target },
                new InvocationHandler()
                {
                    @Override
                    public Object invoke(final Object proxy,
                            final Method method, final Object[] args)
                            throws Throwable
                    {
                        final String path = annotation.basePath()
                                + underScore(method.getName()).toCamelCase()
                                + annotation.extension();
                        if (!map.containsKey(path))
                        {
                            map.put(path, p.get(path,
                                    annotation.contentType(),
                                    annotation.category()));
                        }
                        return map.get(path);
                    }

                    /**
                     * map
                     */
                    private final HashMap<String, WorkflowProcess<Object>> map = new HashMap<String, WorkflowProcess<Object>>();
                }));
    }

    /**
     * constractor injection
     * 
     * @param p
     */
    @Inject
    public TemplateProcessProviderFactoryImpl(final TemplateProcessProvider p)
    {
        this.p = p;
    }

    final TemplateProcessProvider p;
}
