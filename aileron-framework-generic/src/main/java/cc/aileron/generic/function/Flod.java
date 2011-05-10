/**
 *
 */
package cc.aileron.generic.function;

import java.util.Collection;
import java.util.List;

/**
 * @author aileron
 * @param <P>
 * @param <R>
 */
public abstract class Flod<P, R> implements FlodFunction<P, R>,
        CollectionFunction<P, R>
{
    /**
     * @param <P>
     * @param <R>
     * @param list
     * @return {@link FlodApply}
     */
    public static <P, R> FlodApply<P, R> flod(final Collection<P> list)
    {
        return new FlodApply<P, R>()
        {
            @Override
            public R apply(final FlodFunction<P, R> function)
            {
                R ret = null;
                for (final P p : list)
                {
                    ret = function.each(ret, p);
                }
                return ret;
            }
        };
    }

    /**
     * @param <P>
     * @param <R>
     * @param fun
     * @return {@link FlodFunction}
     */
    public static <P, R> FlodFunction<P, R> flod(final FlodFunction<P, R> fun)
    {
        return new Flod<P, R>()
        {
            @Override
            public R each(final R r, final P p)
            {
                return fun.each(r, p);
            }
        };
    }

    /**
     * @return result
     */
    public final R get()
    {
        if (list == null)
        {
            throw new UnsupportedOperationException();
        }
        return Flod.<P, R> flod(list).apply(this);
    }

    @Override
    public R get(final List<P> list)
    {
        return Flod.<P, R> flod(list).apply(this);
    }

    /**
     * default constrator
     */
    public Flod()
    {
        list = null;
    }

    /**
     * @param list
     */
    public Flod(final Collection<P> list)
    {
        this.list = list;
    }

    private final Collection<P> list;
}
