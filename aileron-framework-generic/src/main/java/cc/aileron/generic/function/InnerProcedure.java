/**
 *
 */
package cc.aileron.generic.function;

/**
 * @author aileron
 * @param <P>
 * @param <R>
 */
public abstract class InnerProcedure<P, R>
{
    /**
     * @param object
     */
    protected abstract R call(P object);
}
