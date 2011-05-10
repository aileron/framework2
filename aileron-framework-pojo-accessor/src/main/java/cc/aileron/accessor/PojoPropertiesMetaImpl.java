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

import static cc.aileron.accessor.PojoAccessorMethod.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.aileron.generic.util.SkipList;

/**
 * @author Aileron
 * @param <T>
 */
public class PojoPropertiesMetaImpl<T> implements PojoPropertiesMeta<T>
{
    /**
     * getter-patterns
     */
    public static final String[] getterPatterns = { "get", "is", "to", "" };

    /**
     * map-accessor-keys-set
     */
    public static final HashMap<String, Void> mapAccessorKeys = new HashMap<String, Void>();

    /**
     * map-accesor-keys-get
     */
    public static final String mapGetAccessorKey = "get";

    /**
     * self
     */
    public static final String SELF = "self";

    /**
     * setter-patterns
     */
    public static final String[] setterPatterns = { "set", "" };

    /**
     * error-keys
     */
    private static final int ERR_KEY_WAIT = "wait".hashCode(),
            ERR_KEY_NOTIFY = "notify".hashCode(),
            ERR_KEY_NOFIFYALL = "notifyall".hashCode();

    static
    {
        for (final String key : new String[] { "set", "put" })
        {
            mapAccessorKeys.put(key, null);
        }
    }

    @Override
    public Getter get(final T target, final String name)
    {
        if (SELF.equals(name))
        {
            return new Getter()
            {

                @Override
                public Type genericType()
                {
                    return null;
                }

                @Override
                public Object get()
                {
                    return target;
                }

                @Override
                public PojoPropertiesMetaCategory meta()
                {
                    return PojoPropertiesMetaCategory.SELF;
                }

                @Override
                public Class<?> resultType()
                {
                    return targetClass;
                }
            };
        }
        final AccessorGetter getter = getGetter(name);
        return getter == null ? null : new Getter()
        {
            @Override
            public Type genericType()
            {
                return getter.genericType();
            }

            @Override
            public Object get()
            {
                return getter.get(target, name);
            }

            @Override
            public PojoPropertiesMetaCategory meta()
            {
                return getter.meta();
            }

            @Override
            public Class<?> resultType()
            {
                return getter.resultType();
            }
        };
    }

    /**
     * @param name
     * @return getter
     */
    public AccessorGetter getGetter(final String name)
    {
        /*
         * getter
         */
        final String key = name.toLowerCase();
        for (final String pattern : getterPatterns)
        {
            final String methodName = pattern + key;
            final AccessorGetter accessor = getter.get(methodName);
            if (accessor != null)
            {
                return accessor;
            }
        }

        /*
         * field access
         */
        {
            final AccessorGetter accessor = fields.get(key);
            if (accessor != null)
            {
                return accessor;
            }
        }

        /*
         * get array accessor
         */
        {
            if (isArrayClass)
            {
                return arrayAccessor;
            }
        }

        /*
         * get mapAccessor
         */
        {
            final AccessorGetter accessor = mapGetAccessor;
            if (accessor != null)
            {
                return accessor;
            }
        }

        return null;
    }

    /**
     * @param name
     * @return setter
     */
    public AccessorSetter getSetter(final String name)
    {

        final String key = name.toLowerCase();

        /*
         * scala access
         */
        {
            final AccessorSetter accessorSetter = setter.get(key + "_$eq");
            if (accessorSetter != null)
            {
                return accessorSetter;
            }
        }

        /*
         * setter
         */
        for (final String pattern : setterPatterns)
        {
            final String methodName = pattern + key;
            final AccessorSetter accessor = setter.get(methodName);
            if (accessor != null)
            {
                return accessor;
            }
        }

        /*
         * field access
         */
        {
            final AccessorSetter accessor = fields.get(key);
            if (accessor != null)
            {
                return accessor;
            }
        }

        /*
         * set mapAccessor
         */
        {
            final AccessorMapSetter accessor = mapSetAccessor;
            if (accessor != null)
            {
                return new AccessorSetter()
                {
                    @Override
                    public Class<?> argumentType()
                    {
                        return accessor.argumentType();
                    }

                    @Override
                    public Type genericType()
                    {
                        return accessor.genericType();
                    }

                    @Override
                    public PojoPropertiesMetaCategory meta()
                    {
                        return PojoPropertiesMetaCategory.MAP;
                    }

                    @Override
                    public void set(final Object target, final Object value)
                    {
                        accessor.set(target, key, value);
                    }
                };
            }
        }

        return null;
    }

    @Override
    public List<String> keys(final PojoAccessorMethod method)
    {
        return keys.get(method);
    }

    @Override
    public Setter set(final T target, final String name)
    {
        if (SELF.equals(name))
        {
            return new Setter()
            {
                @Override
                public Class<?> argumentType()
                {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Type genericType()
                {
                    throw new UnsupportedOperationException();
                }

                @Override
                public PojoPropertiesMetaCategory meta()
                {
                    return PojoPropertiesMetaCategory.SELF;
                }

                @Override
                public void set(final Object value)
                {
                    throw new UnsupportedOperationException();
                }
            };
        }
        final AccessorSetter setter = getSetter(name);
        return setter == null ? null : new Setter()
        {
            @Override
            public Class<?> argumentType()
            {
                return setter.argumentType();
            }

            @Override
            public Type genericType()
            {
                return setter.genericType();
            }

            @Override
            public PojoPropertiesMetaCategory meta()
            {
                return setter.meta();
            }

            @Override
            public void set(final Object value)
            {
                setter.set(target, value);
            }
        };
    }

    @Override
    protected void finalize() throws Throwable
    {
        fields.clear();
        getter.clear();
        keys.clear();
    }

    /**
     * @param targetClass
     */
    public PojoPropertiesMetaImpl(final Class<T> targetClass)
    {
        this.targetClass = targetClass;
        isArrayClass = this.targetClass.isArray();

        AccessorGetter mapGetter = null;
        AccessorMapSetter mapSetter = null;

        for (final Method method : targetClass.getMethods())
        {
            /*
             * static メソッドは対象外
             */
            if (Modifier.isStatic(method.getModifiers()))
            {
                continue;
            }

            /*
             * アクセス権限を一旦privateでも頑張る
             */
            if (!method.isAccessible())
            {
                try
                {
                    method.setAccessible(true);
                }
                catch (final SecurityException e)
                {
                    continue;
                }
            }

            /*
             * メソッド名
             */
            final String methodName = method.getName().toLowerCase();

            /*
             * pojo-accessorでcallすると誤動作する為 対象から除外
             */
            final int code = methodName.hashCode();
            if (ERR_KEY_NOFIFYALL == code || ERR_KEY_NOTIFY == code
                    || ERR_KEY_WAIT == code)
            {
                continue;
            }

            if (mapAccessorKeys.containsKey(methodName))
            {
                mapSetter = new AccessorMapSetter()
                {

                    @Override
                    public Class<?> argumentType()
                    {
                        return method.getParameterTypes()[1];
                    }

                    @Override
                    public Type genericType()
                    {
                        return method.getGenericParameterTypes()[1];
                    }

                    @Override
                    public PojoPropertiesMetaCategory meta()
                    {
                        return PojoPropertiesMetaCategory.MAP;
                    }

                    @Override
                    public void set(final Object target, final String key,
                            final Object value)
                    {
                        try
                        {
                            method.invoke(target, key, value);
                        }
                        catch (final Exception e)
                        {
                            throw new Error(e);
                        }
                    }

                };
                continue;
            }
            if (mapGetAccessorKey.equals(methodName))
            {
                mapGetter = new AccessorGetter()
                {
                    @Override
                    public Type genericType()
                    {
                        return method.getGenericReturnType();
                    }

                    @Override
                    public Object get(final Object target, final String key)
                    {
                        final Class<?> type = method.getParameterTypes()[0];
                        try
                        {
                            if (String.class.isAssignableFrom(type))
                            {
                                return method.invoke(target, key);
                            }
                            if (Integer.TYPE.isAssignableFrom(type))
                            {
                                final int idx = Integer.parseInt(key);
                                return method.invoke(target, idx);
                            }
                            if (Map.class.isAssignableFrom(target.getClass()))
                            {
                                final Map<?, ?> map = Map.class.cast(target);
                                if (map.isEmpty())
                                {
                                    return null;
                                }
                                final Object val = map.keySet()
                                        .iterator()
                                        .next()
                                        .getClass()
                                        .getMethod("valueOf", String.class)
                                        .invoke(null, key);
                                return method.invoke(target, val);
                            }
                            throw new Error(type + "の型に対しては、getメソッドが対応していません");
                        }
                        catch (final Exception e)
                        {
                            throw new Error(e);
                        }
                    }

                    @Override
                    public PojoPropertiesMetaCategory meta()
                    {
                        return PojoPropertiesMetaCategory.MAP;
                    }

                    @Override
                    public Class<?> resultType()
                    {
                        return method.getReturnType();
                    }

                };
            }

            switch (method.getParameterTypes().length)
            {
            case 0:
                getter.put(methodName, new AccessorGetter()
                {

                    @Override
                    public Type genericType()
                    {
                        return method.getGenericReturnType();
                    }

                    @Override
                    public Object get(final Object target, final String key)
                    {
                        try
                        {
                            return method.invoke(target);
                        }
                        catch (final Exception e)
                        {
                            throw new Error(targetClass + "@" + key, e);
                        }
                    }

                    @Override
                    public PojoPropertiesMetaCategory meta()
                    {
                        return PojoPropertiesMetaCategory.PROPERTIES;
                    }

                    @Override
                    public Class<?> resultType()
                    {
                        return method.getReturnType();
                    }

                });
                break;

            case 1:
                setter.put(methodName, new AccessorSetter()
                {

                    @Override
                    public Class<?> argumentType()
                    {
                        return method.getParameterTypes()[0];
                    }

                    @Override
                    public Type genericType()
                    {
                        return method.getGenericParameterTypes()[0];
                    }

                    @Override
                    public PojoPropertiesMetaCategory meta()
                    {
                        return PojoPropertiesMetaCategory.PROPERTIES;
                    }

                    @Override
                    public void set(final Object target, final Object value)
                    {
                        try
                        {
                            method.invoke(target, value);
                        }
                        catch (final InvocationTargetException e)
                        {
                            final Throwable c = e.getCause();
                            if (c instanceof Error)
                            {
                                throw (Error) c;
                            }
                            throw new PojoAccessorSetError(target,
                                    methodName,
                                    value,
                                    (Exception) c);
                        }
                        catch (final Exception e)
                        {
                            throw new PojoAccessorSetError(target,
                                    methodName,
                                    value,
                                    e);
                        }
                    }

                });
                break;
            }
        }
        for (final Field field : targetClass.getFields())
        {
            /*
             * static は対象外
             */
            if (Modifier.isStatic(field.getModifiers()))
            {
                continue;
            }

            if (!field.isAccessible())
            {
                try
                {
                    field.setAccessible(true);
                }
                catch (final SecurityException e)
                {
                    continue;
                }
            }
            final String key = field.getName().toLowerCase();
            fields.put(key, new Accessor()
            {

                @Override
                public Class<?> argumentType()
                {
                    return field.getType();
                }

                @Override
                public Type genericType()
                {
                    return field.getGenericType();
                }

                @Override
                public Object get(final Object target, final String key)
                {
                    try
                    {
                        return field.get(target);
                    }
                    catch (final Exception e)
                    {
                        throw new Error(e);
                    }
                }

                @Override
                public PojoPropertiesMetaCategory meta()
                {
                    return PojoPropertiesMetaCategory.FIELDS;
                }

                @Override
                public Class<?> resultType()
                {
                    try
                    {
                        return field.getType();
                    }
                    catch (final Exception e)
                    {
                        throw new Error(e);
                    }
                }

                @Override
                public void set(final Object target, final Object value)
                {
                    try
                    {
                        field.set(target, value);
                    }
                    catch (final Exception e)
                    {
                        throw new PojoAccessorSetError(target, key, value, e);
                    }
                }
            });
        }
        this.mapGetAccessor = mapGetter;
        this.mapSetAccessor = mapSetter;

        this.arrayAccessor = new AccessorGetter()
        {
            @Override
            public Type genericType()
            {
                return targetClass;
            }

            @Override
            public Object get(final Object target, final String key)
            {
                return Array.get(target, Integer.parseInt(key));
            }

            @Override
            public PojoPropertiesMetaCategory meta()
            {
                return PojoPropertiesMetaCategory.ARRAY;
            }

            @Override
            public Class<?> resultType()
            {
                return targetClass;
            }
        };

        /*
         * init keys
         */
        keys.put(GET, new SkipList<String>());
        keys.put(SET, new SkipList<String>());

        /*
         * getter keys
         */
        final HashMap<String, Void> getterKeys = new HashMap<String, Void>();
        for (final String key : getter.keySet())
        {
            getterKeys.put(key, null);
        }
        for (final String key : fields.keySet())
        {
            getterKeys.put(key, null);
        }
        for (final String key : getterKeys.keySet())
        {
            keys.get(GET).add(key);
        }

        /*
         * setter keys
         */
        final HashMap<String, Void> setterKeys = new HashMap<String, Void>();
        for (final String key : setter.keySet())
        {
            setterKeys.put(key, null);
        }
        for (final String key : fields.keySet())
        {
            setterKeys.put(key, null);
        }
        for (final String key : setterKeys.keySet())
        {
            keys.get(SET).add(key);
        }

    }

    /**
     * フィールドの一覧
     */
    public final HashMap<String, Accessor> fields = new HashMap<String, Accessor>();

    /**
     * getter
     */
    public final HashMap<String, AccessorGetter> getter = new HashMap<String, AccessorGetter>();

    /**
     * keys
     */
    public final EnumMap<PojoAccessorMethod, List<String>> keys = new EnumMap<PojoAccessorMethod, List<String>>(PojoAccessorMethod.class);

    /**
     * map-get-accessor
     */
    public final AccessorGetter mapGetAccessor;

    /**
     * map-accessor
     */
    public final AccessorMapSetter mapSetAccessor;

    /**
     * setter
     */
    public final HashMap<String, AccessorSetter> setter = new HashMap<String, AccessorSetter>();

    /**
     * 対象のクラス
     */
    public final Class<T> targetClass;

    private AccessorGetter arrayAccessor;

    private boolean isArrayClass;
}