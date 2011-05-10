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
 * @param <R>
 */
public abstract class Collect<P, R> implements ConvertFunction<P, R>,
        CollectionFunction<P, List<R>>
{
    /**
     * @param <P>
     * @param <R>
     * @param list
     * @return {@link CollectApply}
     */
    public static final <P, R> CollectApply<P, R> collect(
            final Collection<P> list)
    {
        return new CollectApply<P, R>()
        {
            @Override
            public List<R> apply(final ConvertFunction<P, R> function)
            {
                final LinkedList<R> results = new LinkedList<R>();
                for (final P p : list)
                {
                    final R r = function.convert(p);
                    if (r != null)
                    {
                        results.add(r);
                    }
                }
                return results;
            }

        };
    }

    /**
     * @param <P>
     * @param <R>
     * @param conv
     * @return {@link CollectionFunction}
     */
    public static final <P, R> CollectionFunction<P, List<R>> collect(
            final ConvertFunction<P, R> conv)
    {
        return new Collect<P, R>()
        {
            @Override
            public R convert(final P p)
            {
                return conv.convert(p);
            }
        };
    }

    /**
     * @return results
     */
    public final List<R> get()
    {
        if (list == null)
        {
            throw new UnsupportedOperationException();
        }
        return Collect.<P, R> collect(list).apply(this);
    }

    @Override
    public final List<R> get(final List<P> list)
    {
        return Collect.<P, R> collect(list).apply(this);
    }

    /**
     * defualt constractor
     */
    public Collect()
    {
        list = null;
    }

    /**
     * @param list
     */
    public Collect(final Collection<P> list)
    {
        this.list = list;
    }

    private final Collection<P> list;
}
