/**
 *
 */
package cc.aileron.container.model;

/**
 * @author aileron
 */
public interface InstanceFactory
{
    /**
     * @param <T>
     * @param binding
     * @return T
     */
    <T> T getInstance(final Binding<T> binding);

    /**
     * @param <T>
     * @param object
     * @return T
     */
    <T> T injectMember(T object);
}
