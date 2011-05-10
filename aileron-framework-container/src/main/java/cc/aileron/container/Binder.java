/**
 *
 */
package cc.aileron.container;

import java.lang.annotation.Annotation;

import cc.aileron.container.model.Scope;

/**
 * @author aileron
 */
public interface Binder
{
    /**
     * @param <T>
     * @param target
     * @return bind-to
     */
    <T> BinderBind<T> bind(Class<T> target);

    /**
     * @param annotation
     * @param scope
     */
    void bindScope(Class<? extends Annotation> annotation, Scope scope);
}
