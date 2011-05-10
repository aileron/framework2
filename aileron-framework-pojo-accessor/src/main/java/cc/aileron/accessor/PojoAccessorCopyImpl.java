/**
 * 
 */
package cc.aileron.accessor;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class PojoAccessorCopyImpl implements PojoAccessorCopy
{
    @Override
    public <T> To<T> from(final T fromObject)
    {
        final PojoAccessor<T> fromAccessor = pojo.from(fromObject);
        return new To<T>()
        {
            @Override
            public void to(final T toObject)
            {
                bind(toObject);
            }

            private void bind(final T toObject)
            {
                final PojoAccessor<T> toAccessor = pojo.from(toObject);
                for (final String key : fromAccessor.keys(PojoAccessorMethod.GET))
                {
                    try
                    {
                        final PojoAccessorValue fromValue = fromAccessor.to(key), toValue = toAccessor.to(key);
                        if (toValue.exist(PojoAccessorMethod.SET))
                        {
                            toValue.value(fromValue.toObject());
                        }
                    }
                    catch (final PojoAccessorValueNotFoundException e)
                    {
                    }
                    catch (final PojoPropertiesNotFoundException e)
                    {
                    }
                }
            }
        };
    }

    /**
     * @param pojo
     */
    @Inject
    public PojoAccessorCopyImpl(final PojoAccessorManager pojo)
    {
        this.pojo = pojo;
    }

    final PojoAccessorManager pojo;
}
