/**
 *
 */
package cc.aileron.container.model;

import java.lang.annotation.Annotation;

/**
 * @author aileron
 */
public interface ScopeMap
{
    /**
     * @param annotation
     * @return scope
     */
    Scope get(Class<? extends Annotation> annotation);

    /**
     * @param annotation
     * @param scope
     */
    void put(Class<? extends Annotation> annotation, Scope scope);
}
