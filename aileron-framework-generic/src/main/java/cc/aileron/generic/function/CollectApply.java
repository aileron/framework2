package cc.aileron.generic.function;

import java.util.List;

/**
 * @author aileron
 * 
 * @param <P>
 * @param <R>
 */
public interface CollectApply<P, R>
{
    /**
     * @param function
     * @return list
     */
    List<R> apply(ConvertFunction<P, R> function);
}