/**
 *
 */
package cc.aileron.accessor.convertor.dxo;

import cc.aileron.generic.function.ConvertFunction;

/**
 * @author aileron
 */
public @interface PojoAccessorNumberDxo
{
    /**
     * @return dxoクラス
     */
    Class<? extends ConvertFunction<Number, ?>> value();
}
