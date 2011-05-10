/**
 * 
 */
package cc.aileron.accessor;

import com.google.inject.ImplementedBy;

/**
 * @author aileron
 */
@ImplementedBy(PojoAccessorCopyImpl.class)
public interface PojoAccessorCopy
{
    /**
     * @author aileron
     * @param <T>
     */
    interface To<T>
    {
        void to(T object);
    }

    /**
     * @param <T>
     * @param object
     * @return {@link To}
     */
    <T> To<T> from(T object);
}
