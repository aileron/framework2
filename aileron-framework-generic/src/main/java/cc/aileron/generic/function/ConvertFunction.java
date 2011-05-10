/**
 *
 */
package cc.aileron.generic.function;

/**
 * @author aileron
 * @param <P>
 * @param <R>
 */
public interface ConvertFunction<P, R>
{
    /**
     * @param p
     * @return r
     */
    R convert(P p);
}
