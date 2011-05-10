package cc.aileron.generic.function;

/**
 * @author aileron
 * @param <T>
 */
public interface RecursiveFunctionApply<T>
{
    /**
     * @param procedure
     * @param arg
     */
    void apply(final InnerProcedure<T, T> procedure);
}
