/**
 *
 */
package cc.aileron.container;

import java.lang.annotation.Annotation;

/**
 * @author aileron
 */
public interface BinderToInstance
{
    /**
     * @param annotation
     */
    void annotate(Annotation annotation);
}
