/**
 *
 */
package cc.aileron.dao.db.exec;

import org.apache.commons.lang.StringUtils;

import cc.aileron.dao.db.G2DaoNoCondition;
import cc.aileron.dao.db.sql.SqlNotFoundRuntimeException;

import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class G2DaoSqlNameImpl implements G2DaoSqlName
{
    @Override
    public String get(final Object condition, final Class<?> o)
    {
        final String type = o == null ? getName(condition) : o.getSimpleName();
        if (StringUtils.isEmpty(type))
        {
            throw new SqlNotFoundRuntimeException("type name is empty");
        }
        if (condition == G2DaoNoCondition.NO_CONDITION)
        {
            return "All";
        }
        return type;
    }

    /**
     * @param o
     * @return sql-name の取得
     */
    private String getName(final Object o)
    {
        final Class<?> oc = o.getClass();
        if (oc.isEnum())
        {
            return ((Enum<?>) o).name();
        }

        if (oc.isAnonymousClass())
        {
            final Class<?>[] interfaces = oc.getInterfaces();
            if (interfaces.length == 0)
            {
                throw new NotFoundImplementsInterfaceError(oc);
            }
            return oc.getInterfaces()[0].getSimpleName();
        }

        return oc.getSimpleName();
    }
}
