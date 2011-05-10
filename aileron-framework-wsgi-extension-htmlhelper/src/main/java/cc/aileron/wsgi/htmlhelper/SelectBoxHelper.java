/**
 * 
 */
package cc.aileron.wsgi.htmlhelper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cc.aileron.generic.function.ConvertFunction;
import cc.aileron.generic.util.Cast;

/**
 * @author aileron
 */
public class SelectBoxHelper
{
    /**
     * @param <E>
     * @param type
     * @return entrySet
     */
    public static <E extends Enum<E>> ConvertFunction<E, Iterable<Entry<E, Boolean>>> get(
            final Class<E> type)
    {
        final E[] values;
        try
        {
            values = Cast.<E[]> cast(type.getMethod("values").invoke(null));
        }
        catch (final Exception ex)
        {
            throw new Error(ex);
        }
        final int size = values.length;
        return new ConvertFunction<E, Iterable<Entry<E, Boolean>>>()
        {
            @Override
            public Iterable<Entry<E, Boolean>> convert(final E p)
            {
                return new Iterable<Map.Entry<E, Boolean>>()
                {
                    @Override
                    public Iterator<Entry<E, Boolean>> iterator()
                    {
                        return new Iterator<Map.Entry<E, Boolean>>()
                        {
                            @Override
                            public boolean hasNext()
                            {
                                return idx < size;
                            }

                            @Override
                            public Entry<E, Boolean> next()
                            {
                                v = values[idx++];
                                return e;
                            }

                            @Override
                            public void remove()
                            {
                                throw new UnsupportedOperationException();
                            }

                            Entry<E, Boolean> e = new Entry<E, Boolean>()
                            {
                                @Override
                                public E getKey()
                                {
                                    return v;
                                }

                                @Override
                                public Boolean getValue()
                                {
                                    return v == p;
                                }

                                @Override
                                public Boolean setValue(final Boolean value)
                                {
                                    throw new UnsupportedOperationException();
                                }

                            };

                            int idx = 0;
                            E v;
                        };
                    }
                };
            }
        };
    }
}
