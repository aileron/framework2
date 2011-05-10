/**
 * 
 */
package cc.aileron.php;

import java.util.List;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.accessor.PojoAccessorMethod;
import cc.aileron.accessor.PojoAccessorValue;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class Pojo2PhpSourceImpl implements Pojo2PhpSource
{
    @Override
    public String convert(final Object object)
    {
        return "<?php return " + doconvert(pojo.from(object)) + ";";
    }

    private String doconvert(final PojoAccessor<Object> accessor)
    {
        final StringBuilder b = new StringBuilder("Array(");
        final List<String> keys = accessor.keys(PojoAccessorMethod.GET);
        for (final String key : keys)
        {
            System.out.println("KEY:" + key);
            String v = "";
            try
            {
                final PojoAccessorValue value = accessor.to(key);
                final Class<?> type = value.type(PojoAccessorMethod.GET);
                if (type.isPrimitive())
                {
                    v = value.toString();
                    continue;
                }
                if (type == String.class)
                {
                    v = "'" + value.toString() + "'";
                    continue;
                }
                if (Number.class.isAssignableFrom(type))
                {
                    v = value.toString();
                    continue;
                }
                if (Class.class.isAssignableFrom(type))
                {
                    v = "'" + value.toString() + "'";
                    continue;
                }
                v = doconvert(value.accessor(Object.class));
            }
            catch (final PojoAccessorValueNotFoundException e)
            {
            }
            catch (final PojoPropertiesNotFoundException e)
            {
            }
            finally
            {
                b.append("'").append(key).append("'=>").append(v).append(",");
            }

        }
        return b.append(")").toString();
    }

    /**
     * @param pojo
     */
    @Inject
    public Pojo2PhpSourceImpl(final PojoAccessorManager pojo)
    {
        this.pojo = pojo;
    }

    private final PojoAccessorManager pojo;
}
