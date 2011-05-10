/**
 *
 */
package cc.aileron.generic.function;

import java.util.Collection;

import cc.aileron.generic.Procedure;

/**
 * @author aileron
 * @param <P>
 */
public abstract class Each<P> implements EachFunction<P>, Procedure<P>
{
    /**
     * @param <P>
     * @param list
     * @return {@link EachApply}
     */
    public static final <P> EachApply<P> each(final Collection<P> list)
    {
        return new EachApply<P>()
        {
            @Override
            public void apply(final Procedure<P> procedure)
            {
                for (final P p : list)
                {
                    procedure.call(p);
                }
            }
        };
    }

    /**
     * @param <P>
     * @param procedure
     * @return {@link Each}
     */
    public static final <P> EachFunction<P> each(final Procedure<P> procedure)
    {
        return new Each<P>()
        {
            @Override
            public void call(final P p)
            {
                procedure.call(p);
            }
        };
    }

    /**
     * call
     */
    public void call()
    {
        if (list == null)
        {
            throw new UnsupportedOperationException();
        }
        each(list).apply(this);
    }

    @Override
    public void call(final Collection<P> list)
    {
        each(list).apply(this);
    }

    /**
     */
    public Each()
    {
        this.list = null;
    }

    /**
     * @param list
     */
    public Each(final Collection<P> list)
    {
        this.list = list;
    }

    private final Collection<P> list;
}
