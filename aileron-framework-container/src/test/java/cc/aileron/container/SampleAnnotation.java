/**
 *
 */
package cc.aileron.container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author aileron
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.PARAMETER })
public @interface SampleAnnotation
{
    /**
     * @return default-value
     */
    int defaultValue();
}
