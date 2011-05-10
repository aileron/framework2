/**
 *
 */
package cc.aileron.accessor.convertor.dxo;

import cc.aileron.generic.function.ConvertFunction;

/**
 * @author aileron
 */
public @interface PojoAccessorStringDxo
{
    /**
     * @return dxoクラス
     */
    Class<? extends ConvertFunction<String, ?>> value();
}
