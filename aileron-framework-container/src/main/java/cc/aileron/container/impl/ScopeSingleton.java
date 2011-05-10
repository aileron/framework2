/**
 *
 */
package cc.aileron.container.impl;

import java.util.HashMap;

import cc.aileron.container.Provider;
import cc.aileron.container.model.Binding;
import cc.aileron.container.model.Scope;

/**
 * @author aileron
 */
public class ScopeSingleton implements Scope
{
    @SuppressWarnings("unchecked")
    @Override
    public <T> Provider<T> getInstance(final Binding<T> binding,
            final Provider<T> creator)
    {
        final Class<T> interfaceType = binding.interfaceType();
        final HashMap<Object, Provider<?>> amap;
        final HashMap<Object, Provider<?>> imap = map.get(interfaceType);
        if (imap == null)
        {
            amap = new HashMap<Object, Provider<?>>();
            map.put(interfaceType, amap);
        }
        else
        {
            amap = imap;
        }
        final Object annotation = binding.annotation();
        final Provider<?> result = amap.get(annotation);
        if (result != null)
        {
            return (Provider<T>) result;
        }
        final Provider<T> newResult = new Provider<T>()
        {
            @Override
            public T get()
            {
                return instance;
            }

            private final T instance = creator.get();
        };
        amap.put(annotation, newResult);
        return newResult;
    }

    /**
     * instance-map
     */
    private final HashMap<Class<?>, HashMap<Object, Provider<?>>> map = new HashMap<Class<?>, HashMap<Object, Provider<?>>>();
}
