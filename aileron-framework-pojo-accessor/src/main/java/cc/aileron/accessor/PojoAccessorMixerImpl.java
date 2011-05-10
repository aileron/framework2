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
package cc.aileron.accessor;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.generic.util.SkipList;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class PojoAccessorMixerImpl implements PojoAccessorMixer
{
    @Override
    public PojoAccessor<?> mixin(final List<PojoAccessor<?>> accessors,
            final List<Object> parents)
    {
        final InstanceManager i = accessors.get(0).instanceManager();

        final HashMap<String, PojoAccessor<?>> map = new HashMap<String, PojoAccessor<?>>();
        final EnumMap<PojoAccessorMethod, List<String>> keys = new EnumMap<PojoAccessorMethod, List<String>>(PojoAccessorMethod.class);
        keys.put(PojoAccessorMethod.GET, new SkipList<String>());
        keys.put(PojoAccessorMethod.SET, new SkipList<String>());

        final HashMap<String, PojoAccessor<?>> setmap = new HashMap<String, PojoAccessor<?>>();
        for (final PojoAccessor<?> accessor : accessors)
        {
            for (final PojoAccessorMethod method : PojoAccessorMethod.values())
            {
                final List<String> methodKeys = accessor.keys(method);
                keys.get(method).addAll(methodKeys);
            }
            for (final String key : accessor.keys(PojoAccessorMethod.GET))
            {
                map.put(key, accessor);
            }
            for (final String key : accessor.keys(PojoAccessorMethod.SET))
            {
                setmap.put(key, accessor);
            }
        }
        final PojoAccessorManagerLocal manager = this.manager;
        return new PojoAccessor<Object>()
        {
            @Override
            public boolean exist(final String key)
            {
                for (final PojoAccessor<?> accessor : accessors)
                {
                    if (accessor.exist(key))
                    {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public InstanceManager instanceManager()
            {
                return i;
            }

            @Override
            public List<String> keys(final PojoAccessorMethod method)
            {
                return keys.get(method);
            }

            @Override
            public PojoAccessor<?> mixin(final Object... objects)
            {
                final SkipList<PojoAccessor<?>> list = new SkipList<PojoAccessor<?>>();
                list.add(this);
                for (final Object object : objects)
                {
                    list.add(manager.from(object, parents));
                }
                return PojoAccessorMixerImpl.this.mixin(list, parents);
            }

            @Override
            public Iterable<PojoAccessorValue> set(
                    final PojoAccessorMethod method)
            {
                final HashMap<String, PojoAccessor<?>> m = method == PojoAccessorMethod.GET ? map
                        : setmap;
                return new Iterable<PojoAccessorValue>()
                {

                    @Override
                    public Iterator<PojoAccessorValue> iterator()
                    {
                        final Iterator<Entry<String, PojoAccessor<?>>> ite = m.entrySet()
                                .iterator();
                        return new Iterator<PojoAccessorValue>()
                        {
                            @Override
                            public boolean hasNext()
                            {
                                return ite.hasNext();
                            }

                            @Override
                            public PojoAccessorValue next()
                            {
                                final Entry<String, PojoAccessor<?>> e = ite.next();
                                try
                                {
                                    return e.getValue().to(e.getKey());
                                }
                                catch (final PojoAccessorValueNotFoundException e1)
                                {
                                    throw new Error(e1);
                                }
                                catch (final PojoPropertiesNotFoundException e1)
                                {
                                    throw new Error(e1);
                                }
                            }

                            @Override
                            public void remove()
                            {
                                throw new UnsupportedOperationException();
                            }
                        };
                    }
                };
            }

            @Override
            public PojoAccessorValue to(final String key)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                {
                    final PojoAccessor<?> a = map.get(key);
                    if (a != null)
                    {
                        return a.to(key);
                    }
                }
                for (final PojoAccessor<?> a : accessors)
                {
                    if (a.exist(key))
                    {
                        return a.to(key);
                    }
                }
                for (final PojoAccessor<?> a : accessors)
                {
                    try
                    {
                        final PojoAccessorValue result = a.to(key);
                        if (result != null)
                        {
                            return result;
                        }
                    }
                    catch (final PojoAccessorValueNotFoundException e)
                    {
                    }
                    catch (final PojoPropertiesNotFoundException e)
                    {
                    }
                }
                throw new PojoAccessorValueNotFoundException(accessors, key);
            }

            @Override
            public String toString()
            {
                return ReflectionToStringBuilder.toString(this);
            }

            @Override
            public Object toTarget()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * @param manager
     */
    @Inject
    public PojoAccessorMixerImpl(final PojoAccessorManagerLocal manager)
    {
        this.manager = manager;
    }

    private final PojoAccessorManagerLocal manager;
}
