package cc.aileron.generic.function;

/**
 * @author aileron
 * 
 * @param <P>
 */
public interface FillterFunction<P>
{
    /**
     * @param p
     * @return boolean
     */
    boolean each(P p);
}