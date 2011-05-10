/**
 *
 */
package cc.aileron.generic.function;

import java.util.Collection;
import java.util.List;

import cc.aileron.generic.Procedure;

/**
 * Collectionに対して高階関数的な操作を行なう為のメソッド郡
 * 
 * @author aileron
 */
public class CollectionFunctions
{
    /**
     * @param <P>
     * @param <R>
     * @param list
     * @return apply
     */
    public static <P, R> CollectApply<P, R> collect(final Collection<P> list)
    {
        return Collect.<P, R> collect(list);
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
        return Collect.<P, R> collect(conv);
    }

    /**
     * @param <P>
     * @param list
     * @return {@link EachApply}
     */
    public static <P> EachApply<P> each(final Collection<P> list)
    {
        return Each.<P> each(list);
    }

    /**
     * @param <P>
     * @param procedure
     * @return {@link EachFunction}
     */
    public static <P> EachFunction<P> each(final Procedure<P> procedure)
    {
        return Each.<P> each(procedure);
    }

    /**
     * @param <P>
     * @param list
     * @return apply
     */
    public static <P> FillterApply<P> fillter(final Collection<P> list)
    {
        return Fillter.<P> fillter(list);
    }

    /**
     * @param <P>
     * @param fun
     * @return {@link FillterFunction}
     */
    public static <P> FillterFunction<P> fillter(final FillterFunction<P> fun)
    {
        return Fillter.<P> fillter(fun);
    }

    /**
     * @param <P>
     * @param <R>
     * @param list
     * @return apply
     */
    public static <P, R> FlodApply<P, R> flod(final Collection<P> list)
    {
        return Flod.<P, R> flod(list);
    }

    /***
     * @param <P>
     * @param <R>
     * @param fun
     * @return {@link FlodFunction}
     */
    public static <P, R> FlodFunction<P, R> flod(final FlodFunction<P, R> fun)
    {
        return Flod.<P, R> flod(fun);
    }

    /**
     * @param <P>
     * @param <R>
     * @param list
     * @return apply
     */
    public static <P, R> IterateApply<P, R> iterate(final Collection<P> list)
    {
        return Iterate.<P, R> iterate(list);
    }

    /**
     * @param <P>
     * @param <R>
     * @param function
     * @return {@link IterateApply}
     */
    public static final <P, R> CollectionFunction<P, Iterable<R>> iterate(
            final ConvertFunction<P, R> function)
    {
        return Iterate.<P, R> iterate(function);
    }
}
