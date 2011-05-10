package cc.aileron.dao.impl;

import java.util.HashMap;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.dao.G2DaoLocal;
import cc.aileron.dao.G2DaoRegistrar;
import cc.aileron.dao.db.TargetClassNotRegistry;
import cc.aileron.dao.db.exec.G2DaoTransactionManager;
import cc.aileron.generic.util.Cast;

/**
 * @author Aileron
 */
public class G2DaoRegistrarImpl implements G2DaoRegistrar
{
    @Override
    public <T> G2DaoLocal<T> get(final Class<T> targetClass)
    {
        if (!map.containsKey(targetClass))
        {
            throw new TargetClassNotRegistry(targetClass);
        }
        return Cast.<G2DaoLocal<T>> cast(map.get(targetClass));
    }

    @Override
    public G2DaoRegistrarImpl initialize(final InstanceManager instance,
            final G2DaoTransactionManager transactionManager)
    {
        for (final G2DaoLocal<?> dao : map.values())
        {
            dao.initialize(instance, transactionManager);
        }
        return this;
    }

    @Override
    public <T> void registry(final Class<T> type, final G2DaoLocal<T> dao)
    {
        map.put(type, dao);
    }

    /**
     * map
     */
    private final HashMap<Class<?>, G2DaoLocal<?>> map = new HashMap<Class<?>, G2DaoLocal<?>>();
}