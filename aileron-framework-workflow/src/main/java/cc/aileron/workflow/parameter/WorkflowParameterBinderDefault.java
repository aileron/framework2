/**
 *
 */
package cc.aileron.workflow.parameter;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorMethod;
import cc.aileron.accessor.PojoAccessorSetError;
import cc.aileron.accessor.PojoAccessorValue;
import cc.aileron.generic.function.Collect;
import cc.aileron.generic.util.Cast;
import cc.aileron.generic.util.SkipList;
import cc.aileron.workflow.WorkflowParameterBinder;

import com.google.inject.Singleton;

/**
 * WorkflowParameterBinder のデフォルト実装
 * 
 * @author aileron
 */
@Singleton
public class WorkflowParameterBinderDefault implements WorkflowParameterBinder
{
    /*
     * (非 Javadoc)
     * 
     * @see
     * cc.aileron.workflow.WorkflowParameterBinder#bind(cc.aileron.accessor.
     * PojoAccessor, java.util.Map)
     */
    @Override
    public <T> void bind(final PojoAccessor<T> accessor,
            final Map<String, Object> p) throws Exception
    {
        for (final Entry<String, Object> e : p.entrySet())
        {
            final PojoAccessorValue accessorValue = accessor.to(e.getKey());
            try
            {
                accessorValue.value(convert(accessorValue, e.getValue()));
            }
            catch (final PojoAccessorSetError error)
            {
                throw new Exception(error.getMessage(), error.getCause());
            }
            catch (final Error error)
            {
                throw (Exception) error.getCause();
            }
        }
    }

    private Object convert(final PojoAccessorValue accessorValue,
            final Object value)
    {
        if (value == null)
        {
            return null;
        }
        if (!Collection.class.isAssignableFrom(accessorValue.type(PojoAccessorMethod.SET)))
        {
            return (value instanceof Collection) ? Cast.<List<Object>> cast(value)
                    .get(0)
                    : value;
        }
        final boolean isList = Collection.class.isAssignableFrom(value.getClass());
        if (!isList && value.getClass() != String.class)
        {
            return value;
        }

        if (isList
                && ((Collection<?>) value).iterator().next().getClass() != String.class)
        {
            return value;
        }
        final Class<?> type = (Class<?>) ((ParameterizedType) accessorValue.genericType(PojoAccessorMethod.SET)).getActualTypeArguments()[0];
        final Collection<String> tmp = Collection.class.isAssignableFrom(value.getClass()) ? Cast.<Collection<String>> cast(value)
                : new SkipList<String>((String) value);

        return v(type, tmp);
    }

    private <E> List<?> v(final Class<E> ptype, final Collection<String> values)
    {
        if (ptype.equals(String.class))
        {
            return new SkipList<String>(values);
        }
        if (ptype.equals(Boolean.class))
        {
            return new Collect<String, Boolean>(values)
            {
                @Override
                public Boolean convert(final String p)
                {
                    return Boolean.parseBoolean(p);
                }
            }.get();
        }
        if (ptype.equals(Integer.class))
        {
            return new Collect<String, Integer>(values)
            {
                @Override
                public Integer convert(final String p)
                {
                    return Integer.parseInt(p);
                }
            }.get();
        }
        if (ptype.equals(Float.class))
        {
            return new Collect<String, Float>(values)
            {
                @Override
                public Float convert(final String p)
                {
                    return Float.parseFloat(p);
                }
            }.get();
        }
        if (ptype.isEnum())
        {
            return new Collect<String, Object>(values)
            {
                @Override
                public Object convert(final String p)
                {
                    try
                    {
                        try
                        {
                            final int intValue = Integer.parseInt(p);
                            return ptype.getMethod("valueOf", Integer.TYPE)
                                    .invoke(null, intValue);
                        }
                        catch (final NumberFormatException e)
                        {
                            return ptype.getMethod("valueOf", String.class)
                                    .invoke(null, p);
                        }
                    }
                    catch (final Exception e)
                    {
                        throw new Error(e);
                    }
                }
            }.get();
        }
        throw new Error("指定された型についての挙動は実装されていません");
    }
}
