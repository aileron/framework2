/**
 *
 */
package cc.aileron.accessor.convertor.dxo;

import cc.aileron.generic.function.ConvertFunction;

/**
 * @author aileron
 */
public @interface PojoAccessorBooleanDxo
{
    /**
     * @return dxoクラス
     */
    Class<? extends ConvertFunction<Boolean, ?>> value();
}
