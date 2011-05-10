/**
 *
 */
package cc.aileron.generic.function;

import java.util.Collection;

/**
 * @author aileron
 * @param <P>
 */
public interface EachFunction<P>
{
    /**
     * @param list
     */
    void call(Collection<P> list);
}
