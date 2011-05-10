/**
 *
 */
package cc.aileron.container;

/**
 * @author aileron
 * @param <T>
 */
public interface Provider<T>
{
    /**
     * @return T
     */
    T get();
}
