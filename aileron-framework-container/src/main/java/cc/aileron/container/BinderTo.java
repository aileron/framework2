/**
 *
 */
package cc.aileron.container;

import java.lang.annotation.Annotation;

/**
 * @author aileron
 */
public interface BinderTo
{
    /**
     * @param annotation
     * @return {@link BinderAnnotate}
     */
    BinderAnnotate annotate(Annotation annotation);

    /**
     * @param scope
     */
    void in(Class<? extends Annotation> scope);
}
