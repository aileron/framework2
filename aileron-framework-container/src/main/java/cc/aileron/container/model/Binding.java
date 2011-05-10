/**
 *
 */
package cc.aileron.container.model;

import java.lang.annotation.Annotation;

import cc.aileron.container.Provider;

/**
 * @author aileron
 * @param <T>
 */
public interface Binding<T>
{
    /**
     * @return annotation
     */
    Object annotation();

    /**
     * @return implementType
     */
    Class<? extends T> implementType();

    /**
     * @return interfaceType
     */
    Class<T> interfaceType();

    /**
     * @return instance
     */
    Provider<T> provider();

    /**
     * @return scope
     */
    Class<? extends Annotation> scope();
}
