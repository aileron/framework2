/**
 *
 */
package cc.aileron.container.model;

import java.lang.reflect.Type;

/**
 * @author aileron
 * @param <A>
 */
public interface BindingAnnotatedMap<A>
{
    /**
     * @param <B>
     * @param type
     * @param annotation
     * @return binding
     */
    <B> Binding<B> get(Type type, A annotation);

    /**
     * @param <B>
     * @param type
     * @param annotation
     * @param binding
     */
    <B> void put(Type type, A annotation, Binding<B> binding);
}
