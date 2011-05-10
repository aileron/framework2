/**
 *
 */
package cc.aileron.container;

/**
 * @author aileron
 * @param <V>
 * @param <K>
 */
public interface Selector<V, K>
{
    /**
     * @param key
     * @return value
     */
    V get(K key);
}
