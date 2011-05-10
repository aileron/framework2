/**
 *
 */
package cc.aileron.template.flow;

import cc.aileron.generic.function.ConvertFunction;

/**
 * eachのコンテキスト
 * 
 * @author aileron
 * @param <T> 
 */
public interface FlowEachContext<T>
{
    /**
     * @param object 
     * @return object
     */
    T call(T object);

    /**
     * @param object 
     * @return object
     */
    T endCall(T object);

    /**
     * @return index counter
     */
    int i();

    /**
     * @return 終端フラグ
     * @throws UnsupportedOperationException 対応しない場合、例外が送出される
     * 
     */
    boolean z() throws UnsupportedOperationException;

    /**
     * 偶数
     */
    final ConvertFunction<Integer, Boolean> even = new ConvertFunction<Integer, Boolean>()
    {
        @Override
        public Boolean convert(final Integer p)
        {
            return (p & 1) == 0;
        }
    };

    /**
     * 奇数
     */
    final ConvertFunction<Integer, Boolean> odd = new ConvertFunction<Integer, Boolean>()
    {
        @Override
        public Boolean convert(final Integer p)
        {
            return (p & 1) == 1;
        }
    };
}
