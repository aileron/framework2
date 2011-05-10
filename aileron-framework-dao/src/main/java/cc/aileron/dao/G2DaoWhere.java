/**
 *
 */
package cc.aileron.dao;

import cc.aileron.commons.instance.InstanceManager;

/**
 * 対象を特定する為の条件
 * 
 * @author aileron
 * @param <T>
 */
public interface G2DaoWhere<T>
{
    /**
     * @return delete件数
     */
    int delete();

    /**
     * execute
     */
    void execute();

    /**
     * @return {@link G2DaoFinder}
     */
    G2DaoFinder<T> find();

    /**
     * @param <R>
     * @param factory
     * @return {@link G2DaoFinder}
     */
    <R> G2DaoFinder<R> find(InstanceManager.Factory<R, ? super T> factory);

    /**
     * @param value
     * @return 生成された id
     */
    long insert(T value);

    /**
     * @param value
     * @return update件数
     */
    int update(T value);
}
