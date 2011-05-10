/**
 *
 */
package cc.aileron.container;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cc.aileron.container.model.BindingAnnotatedMap;

/**
 * @author aileron
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface InjectOption
{
    /**
     * @return map
     */
    Class<? extends BindingAnnotatedMap<?>> value();
}
