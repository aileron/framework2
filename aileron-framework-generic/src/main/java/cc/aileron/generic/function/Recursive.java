/**
 *
 */
package cc.aileron.generic.function;

/**
 * @author aileron
 * @param <T>
 */
public abstract class Recursive<T> extends InnerProcedure<T, T> implements
        RecursiveObjectApply<T>, RecursiveApply
{
    /**
     * @param <E>
     * @param arg
     * @return {@link RecursiveFunctionApply}
     */
    public static <E> RecursiveFunctionApply<E> recursive(final E arg)
    {
        if (arg == null)
        {
            throw new Error("arg is null");
        }
        return new RecursiveFunctionApply<E>()
        {
            @Override
            public void apply(final InnerProcedure<E, E> function)
            {
                E tmp = arg;
                tmp = function.call(tmp);
                while (tmp != null)
                {
                    tmp = function.call(tmp);
                }
            }
        };
    }

    @Override
    public void apply()
    {
        Recursive.<T> recursive(arg).apply(this);
    }

    @Override
    public void apply(final T arg)
    {
        Recursive.<T> recursive(arg).apply(this);
    }

    /**
     */
    public Recursive()
    {
        this.arg = null;
    }

    /**
     * @param arg
     */
    public Recursive(final T arg)
    {
        this.arg = arg;
    }

    private final T arg;
}
