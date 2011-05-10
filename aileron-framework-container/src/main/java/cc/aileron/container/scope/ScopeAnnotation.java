/**
 *
 */
package cc.aileron.container.scope;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author aileron
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface ScopeAnnotation
{
}
