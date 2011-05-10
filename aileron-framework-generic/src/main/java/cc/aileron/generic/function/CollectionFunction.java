package cc.aileron.generic.function;

import java.util.List;

/**
 * @author aileron
 * @param <P>
 * @param <R>
 */
public interface CollectionFunction<P, R>
{
    /**
     * @param list
     * @return result
     */
    R get(List<P> list);
}