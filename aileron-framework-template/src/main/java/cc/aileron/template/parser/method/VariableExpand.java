/**
 *
 */
package cc.aileron.template.parser.method;

import java.util.List;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValue;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.generic.function.Flod;
import cc.aileron.generic.util.SkipList;

/**
 * @author aileron
 */
public class VariableExpand
{
    /**
     * @param argv
     * @return value
     */
    public String expand(final String argv)
    {
        final List<String> list = new SkipList<String>();
        for (final String token : argv.split("\\|"))
        {
            list.add(token);
        }
        return new Flod<String, String>(list)
        {
            @Override
            public String each(final String rw, final String p)
            {
                final String r = (rw != null ? rw : "");
                if (p.indexOf('\'') != 0)
                {
                    try
                    {
                        final PojoAccessorValue aval = accessor.to(p);
                        final Object object = aval.toObject();
                        return r + (object != null ? object.toString() : "");
                    }
                    catch (final PojoAccessorValueNotFoundException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (final PojoPropertiesNotFoundException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                return r + p.substring(1, p.length() - 1);
            }
        }.get();
    }

    /**
     * @param accessor
     */
    public VariableExpand(final PojoAccessor<?> accessor)
    {
        this.accessor = accessor;
    }

    final PojoAccessor<?> accessor;
}
