/**
 *
 */
package cc.aileron.generic.function;

/**
 * @author aileron
 * @param <P>
 * @param <R>
 */
public interface IterateApply<P, R>
{
    /**
     * @param function
     * @return Iterable<R>
     */
    Iterable<R> apply(ConvertFunction<P, R> function);
}
