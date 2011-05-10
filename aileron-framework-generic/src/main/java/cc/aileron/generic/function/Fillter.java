/**
 *
 */
package cc.aileron.generic.function;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author aileron
 * @param <P>
 */
public abstract class Fillter<P> implements FillterFunction<P>,
        CollectionFunction<P, List<P>>
{
    /**
     * @param <P>
     * @param list
     * @return {@link FillterApply}
     */
    public static <P> FillterApply<P> fillter(final Collection<P> list)
    {
        return new FillterApply<P>()
        {
            @Override
            public List<P> apply(final FillterFunction<P> function)
            {
                final LinkedList<P> results = new LinkedList<P>();
                for (final P p : list)
                {
                    if (!function.each(p))
                    {
                        continue;
                    }
                    results.add(p);
                }
                return results;
            }
        };
    }

    /**
     * @param <P>
     * @param fun
     * @return {@link FillterFunction}
     */
    public static <P> FillterFunction<P> fillter(final FillterFunction<P> fun)
    {
        return new Fillter<P>()
        {
            @Override
            public boolean each(final P p)
            {
                return fun.each(p);
            }
        };
    }

    /**
     * @return result
     */
    public final List<P> get()
    {
        if (list == null)
        {
            throw new UnsupportedOperationException();
        }
        return fillter(list).apply(this);
    }

    @Override
    public List<P> get(final List<P> list)
    {
        return fillter(list).apply(this);
    }

    /**
     * defualt constractor
     */
    public Fillter()
    {
        list = null;
    }

    /**
     * @param list
     */
    public Fillter(final Collection<P> list)
    {
        this.list = list;
    }

    private final Collection<P> list;
}
