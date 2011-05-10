package cc.aileron.generic.function;

/**
 * @author aileron
 * 
 * @param <P>
 * @param <R>
 */
public interface FlodApply<P, R>
{
    /**
     * @param function
     * @return list
     */
    R apply(FlodFunction<P, R> function);
}