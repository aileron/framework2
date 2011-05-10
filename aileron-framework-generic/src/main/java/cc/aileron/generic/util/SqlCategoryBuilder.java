/**
 *
 */
package cc.aileron.generic.util;

import java.util.Map.Entry;

/**
 * enumの数値表現から、sqlの仮想テーブルを作成する
 * 
 * @author aileron
 */
public class SqlCategoryBuilder
{
    /**
     * @param <T>
     * @param category
     * @return sql
     */
    public static <T extends Enum<T>> String select(final Class<T> category)
    {
        final StringBuilder builder = new StringBuilder();
        for (final Entry<Integer, T> e : InterconversionMap.category(category)
                .values())
        {
            builder.append("select " + e.getKey() + " as id,").append("'"
                    + e.toString() + "' as label").append(" union ");
        }
        return builder.toString();
    }
}
