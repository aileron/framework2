/**
 *
 */
package cc.aileron.container;

/**
 * @author aileron
 * @param <T>
 */
public interface BinderBind<T>
{
    /**
     * @param to
     * @return {@link BinderTo}
     */
    BinderTo to(Class<? extends T> to);

    /**
     * @param to
     * @return {@link BinderToInstance}
     */
    BinderToInstance toInstance(T to);

    /**
     * @param to
     * @return {@link BinderTo}
     */
    BinderTo toProvider(Class<? extends Provider<T>> to);

    /**
     * @param provider
     * @return {@link BinderToInstance}
     */
    BinderTo toProvider(Provider<T> provider);
}
