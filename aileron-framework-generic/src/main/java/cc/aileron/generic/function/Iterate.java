/**
 *
 */
package cc.aileron.generic.function;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cc.aileron.generic.ObjectReference;

/**
 * @author aileron
 * @param <P>
 * @param <R>
 */
public abstract class Iterate<P, R> implements ConvertFunction<P, R>,
        CollectionFunction<P, Iterable<R>>
{
    /**
     * @param <P>
     * @param <R>
     * @param list
     * @return {@link IterateApply}
     */
    public static final <P, R> IterateApply<P, R> iterate(
            final Collection<P> list)
    {
        return new IterateApply<P, R>()
        {
            @Override
            public Iterable<R> apply(final ConvertFunction<P, R> function)
            {
                return new Iterable<R>()
                {
                    @Override
                    public Iterator<R> iterator()
                    {
                        final Iterator<P> ite = list.iterator();
                        return new Iterator<R>()
                        {
                            @Override
                            public boolean hasNext()
                            {
                                return ite.hasNext();
                            }

                            @Override
                            public R next()
                            {
                                final P p = ite.next();
                                return function.convert(p);
                            }

                            @Override
                            public void remove()
                            {
                                throw new UnsupportedOperationException();
                            }
                        };
                    }
                };
            }
        };
    }

    /**
     * @param <P>
     * @param <R>
     * @param function
     * @return iterate
     */
    public static final <P, R> CollectionFunction<P, Iterable<R>> iterate(
            final ConvertFunction<P, R> function)
    {
        return new Iterate<P, R>()
        {
            @Override
            public R convert(final P p)
            {
                return function.convert(p);
            }
        };
    }

    /**
     * @param <T>
     * @param referencesets
     * @return {@link Iterable}
     */
    public static <T> Iterable<T> iterate(
            final ObjectReference<ObjectReference<T>> referencesets)
    {
        return new Iterable<T>()
        {
            @Override
            public Iterator<T> iterator()
            {
                final ObjectReference<T> reference = referencesets.get();
                return new Iterator<T>()
                {
                    @Override
                    public boolean hasNext()
                    {
                        return next != null;
                    }

                    @Override
                    public T next()
                    {
                        try
                        {
                            return next;
                        }
                        finally
                        {
                            next = reference.get();
                        }
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }

                    T next = reference.get();
                };
            }
        };
    }

    /**
     * @return result
     */
    public final Iterable<R> get()
    {
        if (list == null)
        {
            throw new UnsupportedOperationException();
        }
        return Iterate.<P, R> iterate(list).apply(this);
    }

    @Override
    public final Iterable<R> get(final List<P> list)
    {
        return Iterate.<P, R> iterate(list).apply(this);
    }

    /**
     * default
     */
    public Iterate()
    {
        this.list = null;
    }

    /**
     * @param list
     */
    public Iterate(final Collection<P> list)
    {
        this.list = list;
    }

    private final Collection<P> list;
}
