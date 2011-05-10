/**
 *
 */
package cc.aileron.container;

/**
 * @author aileron
 */
public interface Injector
{
    /**
     * @param <T>
     * @param type
     * @return instance
     */
    <T> T getInstance(Class<T> type);

    /**
     * @param <T>
     * @param type
     * @return provider
     */
    <T> Provider<T> getProvider(Class<T> type);

    /**
     * @param <T>
     * @param target
     * @return instance
     */
    <T> T injectMember(T target);
}
