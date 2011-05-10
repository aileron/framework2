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
package cc.aileron.dao.db;

import static cc.aileron.dao.db.exec.G2DaoExecuteInvoker.Category.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.commons.instance.InstanceManager.Factory;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.dao.G2DaoExecute;
import cc.aileron.dao.G2DaoFinder;
import cc.aileron.dao.G2DaoLocal;
import cc.aileron.dao.G2DaoMethod;
import cc.aileron.dao.G2DaoPaging;
import cc.aileron.dao.G2DaoWhere;
import cc.aileron.dao.db.exec.G2DaoExecuteInvoker;
import cc.aileron.dao.db.exec.G2DaoExecuteInvoker.Category;
import cc.aileron.dao.db.exec.G2DaoExecutorFactory;
import cc.aileron.dao.db.exec.G2DaoSqlName;
import cc.aileron.dao.db.exec.G2DaoTransactionManager;
import cc.aileron.generic.ObjectContainer;
import cc.aileron.generic.Procedure;
import cc.aileron.generic.util.Cast;
import cc.aileron.generic.util.SoftHashMap;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;

import com.google.inject.Provider;

/**
 * @author Aileron
 * @param <T>
 */
public class G2DaoImpl<T> implements G2DaoLocal<T>
{
    @Override
    public <P> G2DaoExecute<P, T> execute(final Class<P> wtype)
    {
        return new G2DaoExecute<P, T>()
        {
            @Override
            public T value(final P parameter)
            {
                final ObjectContainer<T> c = new ObjectContainer<T>();
                exec(wtype, parameter, new G2DaoResultHandler()
                {
                    @Override
                    public void execute(final ResultSet resultSet)
                            throws SQLException,
                            PojoAccessorValueNotFoundException,
                            PojoPropertiesNotFoundException
                    {
                        final PojoAccessor<T> accessor = provider.get();
                        final List<String> columnNames = getColumnNames(resultSet);
                        if (!resultSet.next())
                        {
                            return;
                        }
                        bind(accessor, columnNames, resultSet);
                        c.value = accessor.toTarget();
                    }

                    @Override
                    public G2DaoMethod method()
                    {
                        return G2DaoMethod.EXEC;
                    }
                });
                return c.value;
            }

            /**
             * @param invoker
             * @param condition
             */
            void exec(final Class<?> wtype, final Object condition,
                    final G2DaoResultHandler invoker)
            {
                if (condition == null)
                {
                    return;
                }
                final Connection connection = transactionManager.get();
                try
                {
                    executor.execute(transactionManager.db(),
                            connection,
                            wtype,
                            condition,
                            invoker);
                }
                catch (final Throwable e)
                {
                    throw new Error("G2Dao ["
                            + type.getName()
                            + "] execute by "
                            + (condition == G2DaoNoCondition.NO_CONDITION ? "all"
                                    : sqlName.get(condition, wtype)),
                            e);
                }
                finally
                {
                    transactionManager.close(connection);
                }
            }
        };
    }

    @Override
    public void initialize()
    {
        execute(INITIALIZE, null, null, null);
    }

    @Override
    public void initialize(final InstanceManager instance,
            final G2DaoTransactionManager transactionManager)
    {
        final G2DaoExecutorFactory executorFactory = instance.get(G2DaoExecutorFactory.class);
        final PojoAccessorManager manager = instance.get(PojoAccessorManager.class);
        final Class<T> type = this.type;
        this.transactionManager = transactionManager;
        this.manager = manager;
        this.sqlName = instance.get(G2DaoSqlName.class);
        this.executor = executorFactory.get(type, sqlMap, instance);
        this.provider = new Provider<PojoAccessor<T>>()
        {
            @Override
            public PojoAccessor<T> get()
            {
                return manager.from(type);
            }
        };
    }

    @Override
    public Number insert(final T target)
    {
        return execute(INSERT, target, G2DaoNoCondition.NO_CONDITION, null);
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public Number update(final T target)
    {
        return execute(UPDATE, target, G2DaoNoCondition.NO_CONDITION, null);
    }

    @Override
    public G2DaoWhere<T> where()
    {
        return where(G2DaoNoCondition.NO_CONDITION);
    }

    @Override
    public <C> G2DaoWhere<T> where(final C condition)
    {
        return where(condition, null);
    }

    @Override
    public <C> G2DaoWhere<T> where(final C condition, final Class<C> wtype)
    {
        return new G2DaoWhere<T>()
        {
            @Override
            public int delete()
            {
                final Number result = G2DaoImpl.this.execute(DELETE,
                        null,
                        condition,
                        wtype);
                return result != null ? result.intValue() : 0;
            }

            @Override
            public void execute()
            {
                G2DaoImpl.this.execute(EXECUTE, null, condition, wtype);
            }

            @Override
            public G2DaoFinder<T> find()
            {
                final PojoAccessorManager manager = G2DaoImpl.this.manager;
                final Provider<PojoAccessor<T>> provider = G2DaoImpl.this.provider;
                return new G2DaoFinder<T>()
                {
                    @Override
                    public void bind(final T object)
                    {
                        if (object == null)
                        {
                            throw new IllegalArgumentException("bindable object is null");
                        }
                        final PojoAccessor<T> accessor = manager.from(object);
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                if (!resultSet.next())
                                {
                                    return;
                                }
                                bind(accessor, columnNames, resultSet);
                            }
                        });
                    }

                    @Override
                    public int count()
                    {
                        final ObjectContainer<Integer> c = new ObjectContainer<Integer>(0);
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException
                            {
                                if (resultSet.next())
                                {
                                    c.value = resultSet.getInt(1);
                                }
                            }

                            @Override
                            public G2DaoMethod method()
                            {
                                return G2DaoMethod.COUNT;
                            }
                        });
                        return c.value;
                    }

                    @Override
                    public void each(final Procedure<T> procedure)
                    {
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                while (resultSet.next())
                                {
                                    final PojoAccessor<T> accessor = provider.get();
                                    bind(accessor, columnNames, resultSet);
                                    final T target = accessor.toTarget();
                                    procedure.call(target);
                                }
                            }

                            @Override
                            public boolean isEach()
                            {
                                return true;
                            }
                        });
                    }

                    @Override
                    public void each(final Procedure<T> procedure,
                            final G2DaoPaging paging)
                    {
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                final int offset = paging.offset();
                                final int size = paging.limit();

                                int i = 0;
                                boolean isNext = offset == 0 ? resultSet.next()
                                        : resultSet.absolute(offset);
                                while (isNext)
                                {
                                    i += 1;

                                    if (size < i)
                                    {
                                        resultSet.close();
                                        break;
                                    }

                                    final PojoAccessor<T> accessor = provider.get();
                                    bind(accessor, columnNames, resultSet);
                                    final T target = accessor.toTarget();
                                    procedure.call(target);

                                    isNext = resultSet.next();
                                }
                            }
                        });
                    }

                    @Override
                    public boolean exist()
                    {
                        final ObjectContainer<Boolean> c = new ObjectContainer<Boolean>(false);
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException
                            {
                                c.value = resultSet.next();
                            }
                        });
                        return c.value;
                    }

                    @Override
                    public List<T> list()
                    {
                        final ObjectContainer<List<T>> c = new ObjectContainer<List<T>>(new ArrayList<T>(50));
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                while (resultSet.next())
                                {
                                    final PojoAccessor<T> accessor = provider.get();
                                    bind(accessor, columnNames, resultSet);
                                    c.value.add(accessor.toTarget());
                                }
                            }
                        });
                        return c.value;
                    }

                    @Override
                    public List<T> list(final G2DaoPaging paging)
                    {
                        final ObjectContainer<List<T>> c = new ObjectContainer<List<T>>(new ArrayList<T>(50));
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                final int offset = paging.offset();
                                final int size = paging.limit();

                                int i = 0;
                                boolean isNext = offset == 0 ? resultSet.next()
                                        : resultSet.absolute(offset);

                                while (isNext)
                                {
                                    i += 1;

                                    if (size < i)
                                    {
                                        resultSet.close();
                                        break;
                                    }

                                    final PojoAccessor<T> accessor = provider.get();
                                    bind(accessor, columnNames, resultSet);
                                    c.value.add(accessor.toTarget());

                                    isNext = resultSet.next();
                                }
                            }
                        });
                        return c.value;
                    }

                    @Override
                    public T one()
                    {
                        final String key;
                        if (isCacheable)
                        {
                            final StringBuilder buff = new StringBuilder();

                            for (final Method method : (wtype != null ? wtype
                                    : condition.getClass().getInterfaces()[0]).getDeclaredMethods())
                            {
                                try
                                {
                                    final Object v = method.invoke(condition);
                                    buff.append(v).append('\0');
                                }
                                catch (final IllegalArgumentException e)
                                {
                                    throw new Error(e);
                                }
                                catch (final IllegalAccessException e)
                                {
                                    throw new Error(e);
                                }
                                catch (final InvocationTargetException e)
                                {
                                    throw new Error(e);
                                }
                            }

                            key = buff.toString();
                            final T val = Cast.<T> cast(cache.get(key));
                            if (val != null)
                            {
                                return val;
                            }
                        }
                        else
                        {
                            key = null;
                        }

                        final ObjectContainer<T> c = new ObjectContainer<T>();
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final PojoAccessor<T> accessor = provider.get();
                                final List<String> columnNames = getColumnNames(resultSet);
                                if (!resultSet.next())
                                {
                                    return;
                                }
                                bind(accessor, columnNames, resultSet);
                                c.value = accessor.toTarget();
                            }
                        });
                        if (isCacheable)
                        {
                            cache.put(key, c.value);
                        }
                        return c.value;
                    }

                };
            }

            @Override
            public <R> G2DaoFinder<R> find(final Factory<R, ? super T> factory)
            {
                final PojoAccessorManager manager = G2DaoImpl.this.manager;
                final Provider<PojoAccessor<R>> provider = new Provider<PojoAccessor<R>>()
                {
                    @Override
                    public PojoAccessor<R> get()
                    {
                        return manager.from(factory.get(G2DaoImpl.this.provider.get()
                                .toTarget()));
                    }
                };
                return new G2DaoFinder<R>()
                {
                    @Override
                    public void bind(final R object)
                    {
                        if (object == null)
                        {
                            throw new IllegalArgumentException("bindable object is null");
                        }
                        final PojoAccessor<R> accessor = manager.from(object);
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                if (!resultSet.next())
                                {
                                    return;
                                }
                                bind(accessor, columnNames, resultSet);
                            }
                        });
                    }

                    @Override
                    public int count()
                    {
                        final ObjectContainer<Integer> c = new ObjectContainer<Integer>(0);
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException
                            {
                                if (resultSet.next())
                                {
                                    c.value = resultSet.getInt(1);
                                }
                            }

                            @Override
                            public G2DaoMethod method()
                            {
                                return G2DaoMethod.COUNT;
                            }
                        });
                        return c.value;
                    }

                    @Override
                    public void each(final Procedure<R> procedure)
                    {
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                while (resultSet.next())
                                {
                                    final PojoAccessor<R> accessor = provider.get();
                                    bind(accessor, columnNames, resultSet);
                                    final R target = accessor.toTarget();
                                    procedure.call(target);
                                }
                            }
                        });
                    }

                    @Override
                    public void each(final Procedure<R> procedure,
                            final G2DaoPaging paging)
                    {
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                final int offset = paging.offset();
                                final int size = paging.limit();

                                int i = 0;
                                boolean isNext = offset == 0 ? resultSet.next()
                                        : resultSet.absolute(offset);
                                while (isNext)
                                {
                                    i += 1;

                                    if (size < i)
                                    {
                                        resultSet.close();
                                        break;
                                    }

                                    final PojoAccessor<R> accessor = provider.get();
                                    bind(accessor, columnNames, resultSet);
                                    final R target = accessor.toTarget();
                                    procedure.call(target);

                                    isNext = resultSet.next();
                                }
                            }
                        });
                    }

                    @Override
                    public boolean exist()
                    {
                        final ObjectContainer<Boolean> c = new ObjectContainer<Boolean>(false);
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException
                            {
                                c.value = resultSet.next();
                            }
                        });
                        return c.value;
                    }

                    @Override
                    public List<R> list()
                    {
                        final ObjectContainer<List<R>> c = new ObjectContainer<List<R>>(new ArrayList<R>(50));
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                while (resultSet.next())
                                {
                                    final PojoAccessor<R> accessor = provider.get();
                                    bind(accessor, columnNames, resultSet);
                                    c.value.add(accessor.toTarget());
                                }
                            }
                        });
                        return c.value;
                    }

                    @Override
                    public List<R> list(final G2DaoPaging paging)
                    {
                        final ObjectContainer<List<R>> c = new ObjectContainer<List<R>>(new ArrayList<R>(50));
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final List<String> columnNames = getColumnNames(resultSet);
                                final int offset = paging.offset();
                                final int size = paging.limit();

                                int i = 0;
                                boolean isNext = offset == 0 ? resultSet.next()
                                        : resultSet.absolute(offset);

                                while (isNext)
                                {
                                    i += 1;

                                    if (size < i)
                                    {
                                        resultSet.close();
                                        break;
                                    }

                                    final PojoAccessor<R> accessor = provider.get();
                                    bind(accessor, columnNames, resultSet);
                                    c.value.add(accessor.toTarget());

                                    isNext = resultSet.next();
                                }
                            }
                        });
                        return c.value;
                    }

                    @Override
                    public R one()
                    {
                        final String key;
                        if (isCacheable)
                        {
                            final StringBuilder buff = new StringBuilder();
                            for (final Method method : (wtype != null ? wtype
                                    : condition.getClass().getInterfaces()[0]).getDeclaredMethods())
                            {
                                try
                                {
                                    buff.append(method.invoke(condition)
                                            .toString()).append('\0');
                                }
                                catch (final IllegalArgumentException e)
                                {
                                    throw new Error(e);
                                }
                                catch (final IllegalAccessException e)
                                {
                                    throw new Error(e);
                                }
                                catch (final InvocationTargetException e)
                                {
                                    throw new Error(e);
                                }
                            }
                            key = buff.toString();
                            final R val = Cast.<R> cast(cache.get(key));
                            if (val != null)
                            {
                                return val;
                            }
                        }
                        else
                        {
                            key = null;
                        }

                        final ObjectContainer<R> c = new ObjectContainer<R>();
                        find(wtype, condition, new G2DaoResultHandler()
                        {
                            @Override
                            public void execute(final ResultSet resultSet)
                                    throws SQLException,
                                    PojoAccessorValueNotFoundException,
                                    PojoPropertiesNotFoundException
                            {
                                final PojoAccessor<R> accessor = provider.get();
                                final List<String> columnNames = getColumnNames(resultSet);
                                if (!resultSet.next())
                                {
                                    return;
                                }
                                bind(accessor, columnNames, resultSet);
                                c.value = accessor.toTarget();
                            }
                        });
                        if (isCacheable)
                        {
                            cache.put(key, c.value);
                        }
                        return c.value;
                    }

                };
            }

            @Override
            public long insert(final T value)
            {
                final Number result = G2DaoImpl.this.execute(INSERT,
                        value,
                        condition,
                        wtype);
                return result != null ? result.longValue() : 0;
            }

            @Override
            public int update(final T value)
            {
                final Number result = G2DaoImpl.this.execute(UPDATE,
                        value,
                        condition,
                        wtype);
                return result != null ? result.intValue() : 0;
            }

            /**
             * @param invoker
             * @param condition
             */
            void find(final Class<?> wtype, final Object condition,
                    final G2DaoResultHandler invoker)
            {
                if (condition == null)
                {
                    return;
                }
                final Connection connection = transactionManager.get();
                try
                {
                    executor.execute(transactionManager.db(),
                            connection,
                            wtype,
                            condition,
                            invoker);
                }
                catch (final Throwable e)
                {
                    throw new Error("G2Dao ["
                            + type.getName()
                            + "] find by "
                            + (condition == G2DaoNoCondition.NO_CONDITION ? "all"
                                    : sqlName.get(condition, wtype)),
                            e);
                }
                finally
                {
                    transactionManager.close(connection);
                }
            }
        };
    }

    /**
     */
    Number execute(final Category category, final T bean,
            final Object condition, final Class<?> wtype)
    {
        final Connection connection = transactionManager.get();
        try
        {
            return executor.execute(transactionManager.db(),
                    connection,
                    wtype,
                    condition,
                    category,
                    bean);
        }
        catch (final SQLException e)
        {
            throw new Error(e);
        }
        catch (final PojoAccessorValueNotFoundException e)
        {
            throw new Error(e);
        }
        catch (final PojoPropertiesNotFoundException e)
        {
            throw new Error(e);
        }
        finally
        {
            transactionManager.close(connection);
        }
    }

    /**
     * @param type
     * @param sqlMap
     * @param isCacheable
     * @throws ParserMethodNotFoundException
     * @throws TemplateSyntaxEexception
     * @throws ResourceNotFoundException
     * @throws IOException
     */
    public G2DaoImpl(final Class<T> type, final G2DaoSqlMap sqlMap,
            final boolean isCacheable) throws IOException,
            ResourceNotFoundException, TemplateSyntaxEexception,
            ParserMethodNotFoundException
    {
        this.type = type;
        this.sqlMap = sqlMap;
        this.isCacheable = isCacheable;
        this.sqlMap.compile(type);
    }

    final SoftHashMap<String, Object> cache = new SoftHashMap<String, Object>();

    G2DaoExecuteInvoker<T> executor;

    final boolean isCacheable;
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    PojoAccessorManager manager;
    Provider<PojoAccessor<T>> provider;
    final G2DaoSqlMap sqlMap;
    G2DaoSqlName sqlName;
    G2DaoTransactionManager transactionManager;
    final Class<T> type;
}