/**
 * 
 */
package cc.aileron.generic;

import java.util.Iterator;
import java.util.List;

import cc.aileron.generic.util.SkipList;

/**
 * @author aileron
 */
public abstract class Functions
{
    /**
     * @param <T>
     * @param a
     * @param b
     * @return add
     */
    public static <T> T[] add(final T[] a, final T[] b)
    {
        final Object dist = new Object[a.length + b.length];
        System.arraycopy(a, 0, dist, 0, a.length);
        System.arraycopy(b, 0, dist, a.length, b.length);
        return cast(dist);
    }

    /**
     * @param <T>
     * @param iterable
     * @param procedure
     */
    public static <T> void apply(final Iterable<T> iterable,
            final Procedure<T> procedure)
    {
        for (final T t : iterable)
        {
            procedure.call(t);
        }
    }

    /**
     * @param <T>
     * @param object
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object object)
    {
        return (T) object;
    }

    /**
     * iterator オブジェクトから Value オブジェクトを取得する
     * 
     * @param <E>
     * @param e
     * @return {@link ObjectReference}
     */
    public static <E> ObjectReference<E> iterate(final Iterator<E> e)
    {
        return new ObjectReference<E>()
        {
            @Override
            public E get()
            {
                if (!e.hasNext())
                {
                    return null;
                }
                return e.next();
            }
        };
    }

    /**
     * @param <E>
     * @param <T>
     * @param iterator
     * @param convertor
     * @return {@link Iterator}
     */
    public static <E, T> Iterator<T> iterate(final Iterator<E> iterator,
            final ObjectProvider<E, T> convertor)
    {
        return iterate(new ObjectReference<T>()
        {
            @Override
            public T get()
            {
                if (!iterator.hasNext())
                {
                    return null;
                }
                return convertor.get(iterator.next());
            }
        });
    }

    /**
     * iteratarオブジェクトの作成
     * 
     * @param <E>
     * @param value
     * @return {@link Iterator}
     */
    public static <E> Iterator<E> iterate(final ObjectReference<E> value)
    {
        return new Iterator<E>()
        {
            @Override
            public boolean hasNext()
            {
                return next != null;
            }

            @Override
            public E next()
            {
                final E result = next;
                next = value.get();
                return result;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }

            E next = value.get();
        };
    }

    /**
     * @param key
     * @param ite
     * @return joined-strings
     */
    public static final String join(final String key, final Iterable<String> ite)
    {
        if (key == null || ite == null)
        {
            throw new IllegalArgumentException(String.format("key=[%s],ite=[%s]",
                    key,
                    ite));
        }
        return join(key, ite.iterator());
    }

    /**
     * @param key
     * @param ite
     * @return join strings
     */
    public static final String join(final String key, final Iterator<String> ite)
    {
        final StringBuilder builder = new StringBuilder();
        while (ite.hasNext())
        {
            final String next = ite.next();
            builder.append(next);
            if (ite.hasNext())
            {
                builder.append(key);
            }
        }
        return builder.toString();
    }

    /**
     * @param key
     * @param ite
     * @return join strings
     */
    public static final String join(final String key,
            final ObjectReference<String> ite)
    {
        return join(key, iterate(ite));
    }

    /**
     * シーケンスに関数を適応する
     * 
     * @param <T>
     * @param <R>
     * @param ts
     * @param function
     * @return 適応済みリスト
     */
    public static <T, R> List<R> map(final Iterable<T> ts,
            final ObjectProvider<T, R> function)
    {
        final List<R> results = new SkipList<R>();
        for (final T t : ts)
        {
            final R result = function.get(t);
            if (result != null)
            {
                results.add(result);
            }
        }
        return results;
    }

    /**
     * @param <T>
     * @param function
     * @return {@link Procedure}
     */
    public static <T> Procedure<T> recur(final ObjectProvider<T, T> function)
    {
        return new Procedure<T>()
        {
            @Override
            public void call(final T object)
            {
                T context = object;
                while (context != null)
                {
                    context = function.get(context);
                }
            }
        };
    }

    /**
     * @param <T>
     * @param object
     * @return {@link ObjectReference}
     */
    public static <T> ObjectReference<T> ref(final T object)
    {
        return new ObjectReference<T>()
        {
            @Override
            public T get()
            {
                return object;
            }
        };
    }

}
