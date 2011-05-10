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

import static cc.aileron.generic.util.Cast.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.generic.PrimitiveWrappers.NumberSetAccessor;
import cc.aileron.generic.PrimitiveWrappers.StringSetAccessor;
import cc.aileron.generic.function.ConvertFunction;
import cc.aileron.generic.util.Cast;
import cc.aileron.generic.util.SkipList;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class PojoAccessorManagerImpl implements PojoAccessorManagerLocal
{
    @Override
    public <T> PojoAccessor<T> from(final Class<T> value)
    {
        return from(instance.get(value));
    }

    @Override
    public <Self> PojoAccessor<Self> from(final Self self)
    {
        return from(self, Collections.emptyList());
    }

    @Override
    public <Self> PojoAccessor<Self> from(final Self self,
            final List<Object> parents)
    {
        final InstanceManager instance = this.instance;
        final PojoAccessorMixer mixer = this.mixer;
        final PojoAccessorManagerImpl manager = this;
        final PojoPropertiesSetManager set = this.set;
        final TypeConvertor convertor = this.convertor;
        return new PojoAccessor<Self>()
        {
            @Override
            public boolean exist(final String key)
            {
                if (self == null)
                {
                    return false;
                }
                try
                {
                    final String setKey = key.split("%")[0];
                    final PojoProperties<Object> p = set.get(setKey,
                            self,
                            parents);
                    if (p.meta(PojoAccessorMethod.GET) == PojoPropertiesMetaCategory.MAP)
                    {
                        return false;
                    }
                }
                catch (final PojoAccessorValueNotFoundException e)
                {
                    return false;
                }
                catch (final PojoPropertiesNotFoundException e)
                {
                    return false;
                }
                return true; // Meta情報を取得してみて例外が発生しない場合は全て true
            }

            @Override
            public InstanceManager instanceManager()
            {
                return instance;
            }

            @Override
            public List<String> keys(final PojoAccessorMethod method)
            {
                if (self == null)
                {
                    return Collections.<String> emptyList();
                }
                return set.get(self).keys(method);
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
                return mixer.mixin(list, parents);
            }

            @Override
            public Iterable<PojoAccessorValue> set(
                    final PojoAccessorMethod method)
            {
                final List<String> keys = keys(method);
                return new Iterable<PojoAccessorValue>()
                {
                    @Override
                    public Iterator<PojoAccessorValue> iterator()
                    {
                        return new Iterator<PojoAccessorValue>()
                        {
                            @Override
                            public boolean hasNext()
                            {
                                return i < size;
                            }

                            @Override
                            public PojoAccessorValue next()
                            {
                                try
                                {
                                    return to(keys.get(i++));
                                }
                                catch (final PojoAccessorValueNotFoundException e)
                                {
                                    throw new Error(e);
                                }
                                catch (final PojoPropertiesNotFoundException e)
                                {
                                    throw new Error(e);
                                }
                            }

                            @Override
                            public void remove()
                            {
                                throw new UnsupportedOperationException();
                            }

                            int i = 0;

                            int size = keys.size();
                        };
                    }
                };
            }

            @Override
            public PojoAccessorValue to(final String key)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                final int dxoidx = key.indexOf('%');
                final boolean isDxo = dxoidx > -1;
                final String dxokey = isDxo ? key.substring(dxoidx + 1) : "";
                final PojoProperties<?> p = set.get(isDxo ? key.substring(0,
                        dxoidx) : key,
                        self,
                        parents);
                return new PojoAccessorValue()
                {
                    @Override
                    public <T> PojoAccessor<T> accessor(final Class<T> type)
                    {
                        final T result = value(type);
                        return manager.from(result,
                                new SkipList<Object>(parents).append(self));
                    }

                    @Override
                    public <T> Iterable<PojoAccessor<T>> accessorIterable(
                            final Class<T> type)
                    {
                        final Iterable<T> result = iterable(type);
                        return new Iterable<PojoAccessor<T>>()
                        {
                            @Override
                            public Iterator<PojoAccessor<T>> iterator()
                            {
                                final Iterator<T> iterator = result.iterator();
                                return new Iterator<PojoAccessor<T>>()
                                {
                                    @Override
                                    public boolean hasNext()
                                    {
                                        return iterator.hasNext();
                                    }

                                    @Override
                                    public PojoAccessor<T> next()
                                    {
                                        final T value = iterator.next();
                                        return manager.from(value,
                                                new SkipList<Object>(parents).append(self));
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
                    public boolean exist(final PojoAccessorMethod method)
                    {
                        return p.exist(method);
                    }

                    @Override
                    public Type genericType(final PojoAccessorMethod method)
                    {
                        return p.genericType(method);
                    }

                    @Override
                    public <T> Iterable<T> iterable(final Class<T> type)
                    {
                        final Object result = val();
                        if (result == null)
                        {
                            return Collections.emptyList();
                        }

                        if (result instanceof Iterable)
                        {
                            return cast(result);
                        }

                        if (result.getClass().isArray())
                        {
                            return cast(Arrays.asList(result));
                        }

                        if (result instanceof Map)
                        {
                            return cast(((Map<?, ?>) result).entrySet());
                        }

                        throw new ClassCastException(key);
                    }

                    @Override
                    public <T> List<T> list(final Class<T> type)
                    {
                        final Object result = val();
                        if (result == null)
                        {
                            return Collections.emptyList();
                        }
                        if (!(result instanceof List<?>))
                        {
                            throw new ClassCastException();
                        }
                        return cast(result);
                    }

                    @Override
                    public Number toNumber()
                    {
                        return value(Number.class);
                    }

                    @Override
                    public Object toObject()
                    {
                        return value(Object.class);
                    }

                    @Override
                    public String toString()
                    {
                        return value(String.class);
                    }

                    @Override
                    public Class<?> type(final PojoAccessorMethod method)
                    {
                        return p.type(method);
                    }

                    @Override
                    public <T> T value(final Class<T> type)
                    {
                        final Object value = val();
                        if (value != null && value.getClass().equals(type))
                        {
                            return Cast.<T> cast(value);
                        }
                        return Cast.<T> cast(convertor.convert(value,
                                p.type(PojoAccessorMethod.GET),
                                type));
                    }

                    @Override
                    public void value(final Object rawValue)
                    {
                        final Class<?> from = rawValue == null ? null
                                : rawValue.getClass();
                        final Class<?> to = p.type(PojoAccessorMethod.SET);
                        if (to == null)
                        {
                            throw new UnsupportedOperationException();
                        }

                        final Object convValue = convertor.convert(rawValue,
                                from,
                                to);
                        /*
                         * set string
                         */
                        if (convValue instanceof String
                                && StringSetAccessor.class.isAssignableFrom(to))
                        {
                            final String value = cast(convValue);
                            final StringSetAccessor accessor = cast(getAccessor());
                            accessor.string(value);
                            p.set(accessor);
                            return;
                        }

                        /*
                         * set number
                         */
                        if (convValue instanceof Number
                                && NumberSetAccessor.class.isAssignableFrom(to))
                        {
                            final Number value = cast(convValue);
                            final NumberSetAccessor accessor = cast(getAccessor());
                            accessor.number(value);
                            p.set(accessor);
                            return;
                        }

                        /*
                         * number set string
                         */
                        if (convValue instanceof String
                                && NumberSetAccessor.class.isAssignableFrom(to))
                        {
                            final String value = cast(convValue);
                            final NumberSetAccessor accessor = cast(getAccessor());
                            try
                            {
                                accessor.number(Integer.valueOf(value));
                            }
                            catch (final NumberFormatException e)
                            {
                                return;
                            }
                            p.set(accessor);
                            return;

                        }

                        /*
                         * number set enum
                         */
                        if (convValue instanceof Number
                                && Enum.class.isAssignableFrom(to))
                        {
                            final Number value = cast(convValue);
                            try
                            {
                                final int intValue = value.intValue();
                                final Method valueOfMethod = to.getMethod("valueOf",
                                        Integer.TYPE);
                                if (valueOfMethod == null
                                        || !Modifier.isStatic(valueOfMethod.getModifiers()))
                                {
                                    throw new Error(to.getName()
                                            + " class is valueOf static method is not found !!!");
                                }

                                final Object enumValue = valueOfMethod.invoke(null,
                                        intValue);
                                p.set(enumValue);
                                return;
                            }
                            catch (final Exception e)
                            {
                                throw new Error(e);
                            }
                        }

                        /*
                         * string set enum
                         */
                        if (convValue instanceof String
                                && Enum.class.isAssignableFrom(to))
                        {
                            final String value = cast(convValue);
                            try
                            {
                                try
                                {
                                    final Integer intValue = Integer.valueOf(value);
                                    final Object enumValue = to.getMethod("valueOf",
                                            Integer.TYPE)
                                            .invoke(null, intValue);
                                    p.set(enumValue);
                                }
                                catch (final NumberFormatException e)
                                {
                                    final Object enumValue = to.getMethod("valueOf",
                                            String.class)
                                            .invoke(null, value);
                                    p.set(enumValue);
                                }
                                return;
                            }
                            catch (final Exception e)
                            {
                                throw new Error(e);
                            }
                        }

                        /*
                         * set value
                         */
                        p.set(convValue);

                    }

                    @SuppressWarnings("unchecked")
                    private <T> T dxo() throws Exception
                    {
                        final Object dxofield;
                        if (dxokey.indexOf('%') == 0)
                        {
                            dxofield = instance.get(Class.forName(dxokey.substring(1)));
                        }
                        else
                        {
                            final Field f = self.getClass().getField(dxokey);
                            f.setAccessible(true);
                            dxofield = f.get(null);
                        }
                        if (dxofield instanceof ConvertFunction)
                        {
                            return (T) ConvertFunction.class.cast(dxofield)
                                    .convert(p.get());
                        }

                        if (dxofield instanceof Class
                                && ConvertFunction.class.isAssignableFrom(Class.class.cast(dxofield)))
                        {
                            final Class<ConvertFunction<Object, T>> dxoclass = (Class<ConvertFunction<Object, T>>) dxofield;
                            return instance.get(dxoclass).convert(p.get());
                        }

                        throw new Error("dxo-class (cc.aileron.generic.function.ConvertFunction) 以外が指定された為エラーです");
                    }

                    /**
                     * get object
                     */
                    private Object getAccessor()
                    {
                        final Object object = p.get();
                        if (object != null)
                        {
                            return object;
                        }
                        return instance.get(p.type(PojoAccessorMethod.GET));
                    }

                    private <T> T val()
                    {
                        if (!isDxo)
                        {
                            return Cast.<T> cast(p.get());
                        }
                        try
                        {
                            return this.<T> dxo();
                        }
                        catch (final Exception e)
                        {
                            throw new Error("error dxo-field"
                                    + self.getClass().toString() + "@" + dxokey,
                                    e);
                        }
                    }
                };
            }

            @Override
            public String toString()
            {
                return PojoAccessor.class.getSimpleName() + "@"
                        + self.getClass().getName();
            }

            @Override
            public Self toTarget()
            {
                return self;
            }
        };
    }

    /**
     * @param instance
     * @param set
     * @param convertor
     * @param mixer
     */
    @Inject
    public PojoAccessorManagerImpl(final InstanceManager instance,
            final PojoPropertiesSetManager set, final TypeConvertor convertor,
            final PojoAccessorMixer mixer)
    {
        this.instance = instance;
        this.set = set;
        this.convertor = convertor;
        this.mixer = mixer;
    }

    private final TypeConvertor convertor;
    private final InstanceManager instance;
    private final PojoAccessorMixer mixer;
    private final PojoPropertiesSetManager set;
}
