/**
 *
 */
package cc.aileron.generic.function;

import cc.aileron.generic.Procedure;

/**
 * @author aileron
 * @param <T>
 */
public interface EachApply<T>
{
    /**
     * @param procedure
     */
    void apply(Procedure<T> procedure);
}
