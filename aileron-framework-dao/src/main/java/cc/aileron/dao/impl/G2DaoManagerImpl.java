package cc.aileron.dao.impl;

import java.sql.SQLException;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.dao.G2Dao;
import cc.aileron.dao.G2DaoManager;
import cc.aileron.dao.G2DaoRegistrar;
import cc.aileron.dao.G2DaoTransaction;
import cc.aileron.dao.db.exec.G2DaoTransactionManager;
import cc.aileron.dao.db.exec.G2DaoTransactionManager.Category;

import com.google.inject.Inject;

/**
 * @author Aileron
 */
public class G2DaoManagerImpl implements G2DaoManager
{
    @Override
    public <T> G2Dao<T> from(final Class<T> targetClass)
    {
        return registrar.get(targetClass);

    }

    @Override
    public G2DaoTransaction transaction()
    {
        return transaction;
    }

    /**
     * @param registrar
     * @param instance
     * @param transaction
     */
    @Inject
    public G2DaoManagerImpl(final InstanceManager instance,
            final G2DaoRegistrar registrar,
            final G2DaoTransactionManager transaction)
    {
        this.registrar = registrar.initialize(instance, transaction);
        this.transaction = new G2DaoTransaction()
        {

            @Override
            public void begin()
            {
                transaction.start();
            }

            @Override
            public void commit() throws SQLException
            {
                transaction.end(Category.COMMIT);
            }

            @Override
            public void rollback() throws SQLException
            {
                transaction.end(Category.ROLLBACK);
            }
        };
    }

    private final G2DaoRegistrar registrar;
    private final G2DaoTransaction transaction;
}