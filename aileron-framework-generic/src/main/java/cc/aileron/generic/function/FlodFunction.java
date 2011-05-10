package cc.aileron.generic.function;

/**
 * 
 * @author aileron
 * 
 * @param <P>
 * @param <R>
 */
public interface FlodFunction<P, R>
{
    /**
     * @param r
     * @param p
     * @return result
     */
    R each(R r, P p);
}