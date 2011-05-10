/**
 *
 */
package cc.aileron.container.model;

import cc.aileron.container.Provider;

/**
 * @author aileron
 */
public interface InstanceMap
{
    /**
     * @param <T>
     * @param binding
     * @return instance
     */
    <T> Provider<T> getProvider(Binding<T> binding);

    /**
     * @param <T>
     * @param object
     * @return T
     */
    <T> T injectMember(T object);
}
