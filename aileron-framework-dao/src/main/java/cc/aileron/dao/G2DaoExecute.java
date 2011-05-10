/**
 * 
 */
package cc.aileron.dao;

/**
 * @author aileron
 * @param <P> 
 * @param <R> 
 */
public interface G2DaoExecute<P, R>
{
    /**
     * @param parameter
     * @return value
     */
    R value(P parameter);
}
