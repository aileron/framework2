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

import static cc.aileron.dao.db.exec.G2DaoTransactionManager.Category.*;

import java.lang.annotation.Annotation;
import java.sql.Connection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import cc.aileron.dao.db.exec.G2DaoTransactionManager;
import cc.aileron.dao.db.exec.G2DaoTransactionManagerImpl;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

/**
 * @author Aileron
 * 
 * 単純にもういらないモジュール
 * 
 */
@Deprecated
public class G2DaoConnectionModule implements Module, Provider<Connection>,
        MethodInterceptor
{
    @Override
    public void configure(final Binder binder)
    {
        /*
         * bind connection to provider
         */
        binder.bind(Connection.class).toProvider(this);

        /*
         * bind intercept
         */
        binder.bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(annotation),
                this);

        /*
         * bind transactionManager
         */
        binder.bind(G2DaoTransactionManager.class)
                .toInstance(transactionManager);
    }

    @Override
    public Connection get()
    {
        return transactionManager.get();
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable
    {
        try
        {
            transactionManager.start();
            final Object result = invocation.proceed();
            transactionManager.end(COMMIT);
            return result;
        }
        catch (final Throwable t)
        {
            transactionManager.end(ROLLBACK);
            throw t;
        }
        finally
        {
            transactionManager.close();
        }
    }

    /**
     * @param connection
     * @throws Exception
     */
    public G2DaoConnectionModule(final Provider<Connection> connection)
            throws Exception
    {
        this.annotation = G2Transactional.class;
        this.transactionManager = new G2DaoTransactionManagerImpl(connection);
    }

    /**
     * @param connection
     * @param annotation
     * @throws Exception
     */
    public G2DaoConnectionModule(final Provider<Connection> connection,
            final Class<? extends Annotation> annotation) throws Exception
    {
        this.annotation = annotation;
        this.transactionManager = new G2DaoTransactionManagerImpl(connection);
    }

    private final Class<? extends Annotation> annotation;
    private final G2DaoTransactionManagerImpl transactionManager;
}
