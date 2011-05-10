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
@Target( { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD })
public @interface Inject
{
}
