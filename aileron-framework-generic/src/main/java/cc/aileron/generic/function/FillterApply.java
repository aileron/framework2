package cc.aileron.generic.function;

import java.util.List;


/**
 * @author aileron
 * 
 * @param <P>
 */
public interface FillterApply<P>
{
    /**
     * @param function
     * @return list
     */
    List<P> apply(FillterFunction<P> function);
}