/**
 * 
 */
package cc.aileron.dao;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.commons.util.ClassPattrnFinderUtils;
import cc.aileron.dao.db.G2DaoConnectionProvider;
import cc.aileron.dao.db.G2Transactional;
import cc.aileron.dao.db.exec.G2DaoTransactionManager;
import cc.aileron.dao.db.exec.G2DaoTransactionManager.Category;
import cc.aileron.dao.db.exec.G2DaoTransactionManagerImpl;
import cc.aileron.dao.impl.G2DaoManagerImpl;
import cc.aileron.dao.impl.G2DaoRegistrarImpl;
import cc.aileron.generic.util.SkipList;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

/**
 * マルチコネクション対応ver
 * 
 * @author aileron
 */
public class G2MDaoModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        for (final Entry<Class<? extends Annotation>, G2DaoTransactionManager> e : tmap.entrySet())
        {
            final Provider<G2DaoManager> p = new Provider<G2DaoManager>()
            {
                @Override
                public G2DaoManager get()
                {
                    return new G2DaoManagerImpl(instance,
                            rmap.get(e.getKey()),
                            e.getValue());
                }

                @Inject
                InstanceManager instance;
            };
            if (e.getKey() != null)
            {
                binder.bind(G2DaoManager.class)
                        .annotatedWith(e.getKey())
                        .toProvider(p);
                binder.bind(Connection.class)
                        .annotatedWith(e.getKey())
                        .toProvider(e.getValue());
            }
            else
            {
                binder.bind(G2DaoManager.class).toProvider(p);
                binder.bind(Connection.class).toProvider(e.getValue());
            }
        }

        binder.bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(annotation),
                new MethodInterceptor()
                {
                    @Override
                    public Object invoke(final MethodInvocation invocation)
                            throws Throwable
                    {
                        try
                        {
                            for (final G2DaoTransactionManager t : transactionManagers)
                            {
                                t.start();
                            }
                            final Object result = invocation.proceed();
                            for (final G2DaoTransactionManager t : transactionManagers)
                            {
                                t.end(Category.COMMIT);
                            }
                            return result;
                        }
                        catch (final Throwable th)
                        {
                            for (final G2DaoTransactionManager t : transactionManagers)
                            {
                                t.end(Category.ROLLBACK);
                            }
                            throw th;
                        }
                        finally
                        {
                            for (final G2DaoTransactionManager t : transactionManagers)
                            {
                                t.close();
                            }
                        }
                    }
                });
    }

    /**
     * @param <T>
     * @param factory
     * @param type
     * @throws Exception
     */
    private <T> void registry(final G2DaoRegistrar registrar,
            final G2DaoFactory factory, final Class<T> type,
            final boolean isCacheable) throws Exception
    {
        registrar.<T> registry(type, factory.<T> create(type, isCacheable));
    }

    /**
     * @param packages
     * @param settings
     * @throws Exception
     * @throws ResourceNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     */
    public G2MDaoModule(final Collection<Package> packages,
            final G2DaoNetworkSetting... settings) throws IOException,
            URISyntaxException, ResourceNotFoundException, Exception
    {
        for (final Package target : packages)
        {
            for (final G2DaoNetworkSetting s : settings)
            {
                tmap.put(s.annotation(),
                        new G2DaoTransactionManagerImpl(new G2DaoConnectionProvider(s.dataSource())));
                final G2DaoRegistrar registrar = new G2DaoRegistrarImpl();
                for (final Class<?> model : ClassPattrnFinderUtils.getClassNameList(target))
                {
                    registry(registrar, factory, model, s.isCacheable());
                }
                rmap.put(s.annotation(), registrar);

            }

        }

        int i = 0;
        transactionManagers = new G2DaoTransactionManager[settings.length];
        for (final Entry<Class<? extends Annotation>, G2DaoTransactionManager> e : tmap.entrySet())
        {
            transactionManagers[i++] = e.getValue();
        }
    }

    /**
     * @param target
     *            G2Dao管理対象となるパッケージ
     * @param settings
     *            DBとの接続ソース
     * @throws Exception
     * @throws ResourceNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     */
    public G2MDaoModule(final Package target,
            final G2DaoNetworkSetting... settings) throws IOException,
            URISyntaxException, ResourceNotFoundException, Exception
    {
        this(new SkipList<Package>(target), settings);
    }

    /**
     * annotation
     */
    final Class<? extends Annotation> annotation = G2Transactional.class;

    /**
     * factory
     */
    final G2DaoFactory factory = G2DaoCategory.RDBMS.getFactory();

    /**
     * r-map
     */
    final HashMap<Class<? extends Annotation>, G2DaoRegistrar> rmap = new HashMap<Class<? extends Annotation>, G2DaoRegistrar>();

    /**
     * transaction-manager-map
     */
    final HashMap<Class<? extends Annotation>, G2DaoTransactionManager> tmap = new HashMap<Class<? extends Annotation>, G2DaoTransactionManager>();

    /**
     * transactionManagers
     */
    final G2DaoTransactionManager[] transactionManagers;
}
