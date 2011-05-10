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

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorMethod;
import cc.aileron.accessor.PojoAccessorValue;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.util.ResourceUtils;
import cc.aileron.generic.util.Cast;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.reader.TemplateReader;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class FlowMethodProviderMapImpl implements FlowMethodProviderMap
{
    @Override
    public FlowMethodProvider get(final FlowCategory category)
    {
        return map.get(category);
    }

    /**
     * @param reader 
     */
    @Inject
    public FlowMethodProviderMapImpl(final TemplateReader reader)
    {
        map = new EnumMap<FlowCategory, FlowMethodProvider>(FlowCategory.class);
        map.put(DEF, new FlowDef());
        map.put(EACH, new FlowEach());
        map.put(WITH, new FlowWith());
        map.put(COMMENT, new FlowComment());
        map.put(INCLUDE, new FlowInclude(reader));
    }

    private final EnumMap<FlowCategory, FlowMethodProvider> map;
}

class FlowComment implements FlowMethodProvider
{
    @Override
    public Object get(final String args)
    {
        return null;
    }
}

class FlowDef implements FlowMethodProvider
{
    @Override
    public FlowDefMethod get(final String args)
    {
        return new FlowDefMethod()
        {
            @Override
            public boolean call(final TemplateContext context)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                final String[] token = args.split(" @ ");
                final String key = token[0].replace("!", "").trim();
                final String compKey;
                if (token.length >= 2)
                {
                    compKey = token[1].replace("!", "").trim();
                }
                else
                {
                    compKey = null;
                }
                final boolean isReverse = args.trim().endsWith("!");
                final PojoAccessor<?> pojo = context.getAccessor();
                final PojoAccessorValue accessor = pojo.to(key);
                if (!accessor.exist(PojoAccessorMethod.GET))
                {
                    return isReverse;
                }
                final Object object = accessor.toObject();
                final Class<?> type = accessor.type(PojoAccessorMethod.GET);
                if (type.isEnum() || object instanceof Enum)
                {
                    final boolean enumValue = enumValue(accessor, compKey);
                    return isReverse ? !enumValue : enumValue;
                }
                if (compKey != null && object instanceof EnumSet)
                {
                    final EnumSet<?> set = accessor.value(EnumSet.class);
                    if (set.isEmpty())
                    {
                        return isReverse;
                    }
                    try
                    {
                        final Object v = set.iterator()
                                .next()
                                .getClass()
                                .getMethod("valueOf", String.class)
                                .invoke(null, compKey);
                        final boolean result = set.contains(v);
                        return isReverse ? !result : result;
                    }
                    catch (final Exception e)
                    {
                        throw new Error(e);
                    }
                }
                if (Collection.class.isAssignableFrom(type))
                {
                    final Collection<?> collection = accessor.value(Collection.class);
                    final boolean exist = collection == null ? false
                            : !collection.isEmpty();
                    return isReverse ? !exist : exist;
                }
                if (compKey != null
                        && (Integer.class.isAssignableFrom(type) || Integer.TYPE.equals(type)))
                {
                    final Integer value = accessor.value(Integer.class);
                    final Integer compValue = Integer.parseInt(compKey);
                    final boolean comp = compValue.equals(value);
                    return isReverse ? !comp : comp;
                }
                if (compKey != null
                        && (Float.class.isAssignableFrom(type) || Float.TYPE.equals(type)))
                {
                    final Float value = accessor.value(Float.class);
                    final Float compValue = Float.parseFloat(compKey);
                    final boolean comp = compValue.equals(value);
                    return isReverse ? !comp : comp;
                }

                final boolean boolValue = boolValue(accessor, compKey);
                final boolean result = isReverse ? !boolValue : boolValue;
                return result;
            }

            /**
             * @param accessor
             * @param compKey
             * @return bool
             */
            private boolean boolValue(final PojoAccessorValue accessor,
                    final String compKey)
            {
                final boolean value = accessor.value(Boolean.class);
                if (compKey == null)
                {
                    return value;
                }
                final Boolean compValue = Boolean.parseBoolean(compKey);
                return compValue.equals(value);
            }

            /**
             * @param accessor
             * @param compKey
             * @return bool
             */
            private boolean enumValue(final PojoAccessorValue accessor,
                    final String compKey)
            {
                final Enum<?> value = accessor.value(Enum.class);
                if (compKey == null)
                {
                    return value != null;
                }
                if (value == null)
                {
                    return false;
                }
                return compKey.equals(value.name());
            }
        };
    }
}

class FlowEach implements FlowMethodProvider
{
    @Override
    public FlowEachMethod get(final String args)
    {
        final String[] token = args.split(" ");
        final String key = token[0];
        final Class<? extends FlowEachContext<?>> contextClass;
        if (token.length >= 2)
        {
            try
            {
                contextClass = Cast.cast(Class.forName(token[1])
                        .asSubclass(FlowEachContext.class));
            }
            catch (final ClassNotFoundException e)
            {
                throw new Error(e);
            }
        }
        else
        {
            contextClass = FlowEachContextDefault.class;
        }

        return new FlowEachMethod()
        {
            @Override
            public Iterable<PojoAccessor<Object>> call(
                    final TemplateContext context)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {

                return context.getAccessor()
                        .to(key)
                        .accessorIterable(Object.class);
            }

            @Override
            public FlowEachContext<Object> context(final TemplateContext context)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                return Cast.cast(context.getAccessor()
                        .instanceManager()
                        .get(contextClass));
            }
        };
    }
}

class FlowInclude implements FlowMethodProvider
{
    @Override
    public FlowIncludeMethod get(final String args)
    {
        final FlowComponent include;
        try
        {
            include = reader.read(ResourceUtils.resource(args).toString());
        }
        catch (final Exception e)
        {
            throw new Error(e);
        }
        return new FlowIncludeMethod()
        {
            @Override
            public FlowComponent call()
            {
                return include;
            }
        };
    }

    /**
     * @param reader
     */
    public FlowInclude(final TemplateReader reader)
    {
        this.reader = reader;
    }

    private final TemplateReader reader;
}

class FlowWith implements FlowMethodProvider
{
    @Override
    public FlowWithMethod get(final String args)
    {
        return new FlowWithMethod()
        {
            @Override
            public PojoAccessor<?> call(final TemplateContext context)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                return context.getAccessor().to(args).accessor(Object.class);
            }
        };
    }
}