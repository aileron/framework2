/**
 *
 */
package cc.aileron.container;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author aileron
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ImplementedBy
{
    /**
     * @return implementation type
     */
    Class<?> value();
}
