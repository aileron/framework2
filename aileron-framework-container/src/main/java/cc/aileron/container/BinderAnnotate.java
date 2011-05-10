/**
 *
 */
package cc.aileron.container;

import java.lang.annotation.Annotation;

/**
 * @author aileron
 */
public interface BinderAnnotate
{
    /**
     * @param scope
     */
    void in(Class<? extends Annotation> scope);
}
