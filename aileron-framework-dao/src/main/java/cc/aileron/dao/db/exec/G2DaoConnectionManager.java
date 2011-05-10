/**
 * 
 */
package cc.aileron.dao.db.exec;

import cc.aileron.dao.G2DaoManager;

/**
 * @author aileron
 */
public interface G2DaoConnectionManager
{
    /**
     * @param manager
     * @return {@link G2DaoTransactionManager}
     */
    G2DaoTransactionManager get(Class<? extends G2DaoManager> manager);
}
