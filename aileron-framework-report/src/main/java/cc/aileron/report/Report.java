/**
 *
 */
package cc.aileron.report;

/**
 * @author aileron
 * @param <T> 
 */
public interface Report<T>
{
    /**
     * @param category
     * @param object
     */
    void send(final T object);
}
