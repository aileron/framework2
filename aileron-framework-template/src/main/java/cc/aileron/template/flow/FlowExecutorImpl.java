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
package cc.aileron.template.flow;

import static cc.aileron.template.flow.FlowCategory.*;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.parser.ParserMethod;

import com.google.inject.Inject;

/**
 * 
 * flow-executor
 * 
 * @author Aileron
 * 
 */
public class FlowExecutorImpl implements FlowExecutor
{
    @Override
    public void call(final TemplateContext context)
            throws FlowMethodNotFoundError, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        this.call(context, root);
    }

    @Override
    public FlowExecutor setComponnent(final FlowComponent component)
    {
        root = component;
        return this;
    }

    /**
     * @param context
     * @param component
     * @throws FlowMethodNotFoundError
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    void call(final TemplateContext context, final FlowComponent component)
            throws FlowMethodNotFoundError, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final IF instance = map.get(component.category);
        if (instance == null)
        {
            throw new FlowMethodNotFoundError(component.category);
        }
        instance.call(component.self, context, component.children);
    }

    /**
     * constractor injection
     */
    @Inject
    public FlowExecutorImpl()
    {
        map.put(EXECUTE, new Execute());
        map.put(DEF, new Def(this));
        map.put(EACH, new Each(this));
        map.put(SEQUENTIAL, new Sequential(this));
        map.put(WITH, new With(this));
        map.put(COMMENT, new Comment());
        map.put(INCLUDE, new Include(this));
    }

    private final EnumMap<FlowCategory, IF> map = new EnumMap<FlowCategory, IF>(FlowCategory.class);

    private FlowComponent root;
}

/**
 * @author Aileron
 */
class Comment implements IF
{
    @Override
    public void call(final Object self, final TemplateContext context,
            final List<FlowComponent> children)
    {
    }
}

/**
 * @author Aileron
 */
class Def implements IF
{
    @Override
    public void call(final Object self, final TemplateContext context,
            final List<FlowComponent> children)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final FlowDefMethod method = (FlowDefMethod) self;
        if (method.call(context) == false)
        {
            return;
        }
        for (final FlowComponent component : children)
        {
            executor.call(context, component);
        }
    }

    /**
     * @param executor
     */
    public Def(final FlowExecutorImpl executor)
    {
        this.executor = executor;
    }

    private final FlowExecutorImpl executor;
}

/**
 * @author Aileron
 */
class Each implements IF
{
    @Override
    public void call(final Object self, final TemplateContext context,
            final List<FlowComponent> children)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final PojoAccessor<?> oldAccessor = context.getAccessor();
        final FlowEachMethod method = (FlowEachMethod) self;
        final FlowEachContext<Object> c = method.context(context);
        final Iterator<PojoAccessor<Object>> ite = method.call(context)
                .iterator();
        while (ite.hasNext())
        {
            final PojoAccessor<?> pojoAccessor = ite.next();
            final Object raw = pojoAccessor.toTarget();
            Object object;
            if (ite.hasNext())
            {
                object = c.call(raw);
            }
            else
            {
                object = c.endCall(raw);
            }
            if (object == null)
            {
                continue;
            }
            context.setAccessor(pojoAccessor.mixin(c));
            for (final FlowComponent component : children)
            {
                executor.call(context, component);
            }
        }
        context.setAccessor(oldAccessor);
    }

    /**
     * @param executor
     */
    public Each(final FlowExecutorImpl executor)
    {
        this.executor = executor;
    }

    private final FlowExecutorImpl executor;
}

/**
 * @author Aileron
 */
class Execute implements IF
{
    @Override
    public void call(final Object self, final TemplateContext context,
            final List<FlowComponent> children)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final ParserMethod method = (ParserMethod) self;
        method.call(context);
    }
}

/**
 * @author Aileron
 */
interface IF
{
    /**
     * @param self
     * @param context
     * @param children
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    void call(Object self, TemplateContext context, List<FlowComponent> children)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException;
}

/**
 * @author aileron
 */
class Include implements IF
{
    @Override
    public void call(final Object self, final TemplateContext context,
            final List<FlowComponent> children)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final FlowIncludeMethod method = (FlowIncludeMethod) self;
        executor.call(context, method.call());
    }

    /**
     * @param executor
     */
    public Include(final FlowExecutorImpl executor)
    {
        this.executor = executor;
    }

    private final FlowExecutorImpl executor;
}

/**
 * @author Aileron
 */
class Sequential implements IF
{
    @Override
    public void call(final Object self, final TemplateContext context,
            final List<FlowComponent> children)
            throws FlowMethodNotFoundError, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        for (final FlowComponent component : children)
        {
            executor.call(context, component);
        }
    }

    /**
     * @param executor
     */
    public Sequential(final FlowExecutorImpl executor)
    {
        this.executor = executor;
    }

    private final FlowExecutorImpl executor;
}

/**
 * @author Aileron
 */
class With implements IF
{
    @Override
    public void call(final Object self, final TemplateContext context,
            final List<FlowComponent> children)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final PojoAccessor<?> oldAccessor = context.getAccessor();
        final FlowWithMethod method = (FlowWithMethod) self;
        context.setAccessor(method.call(context));
        for (final FlowComponent component : children)
        {
            executor.call(context, component);
        }
        context.setAccessor(oldAccessor);
    }

    /**
     * @param executor
     */
    public With(final FlowExecutorImpl executor)
    {
        this.executor = executor;
    }

    private final FlowExecutorImpl executor;
}