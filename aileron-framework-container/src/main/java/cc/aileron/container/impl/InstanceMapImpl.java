/**
 *
 */
package cc.aileron.container.impl;

import java.lang.annotation.Annotation;

import cc.aileron.container.Provider;
import cc.aileron.container.model.Binding;
import cc.aileron.container.model.BindingMap;
import cc.aileron.container.model.InstanceFactory;
import cc.aileron.container.model.InstanceMap;
import cc.aileron.container.model.Scope;
import cc.aileron.container.model.ScopeMap;

/**
 * @author aileron
 */
class InstanceMapImpl implements InstanceMap
{
    @Override
    public <T> Provider<T> getProvider(final Binding<T> binding)
    {
        final Provider<T> provider = new Provider<T>()
        {
            @Override
            public T get()
            {
                return instanceFactory.getInstance(binding);
            }
        };
        final Class<? extends Annotation> scopeAnnotation = binding.scope();
        final Scope scope = scopeMap.get(scopeAnnotation);
        if (scope == null)
        {
            return provider;
        }
        return scope.getInstance(binding, provider);
    }

    @Override
    public <T> T injectMember(final T object)
    {
        return instanceFactory.injectMember(object);
    }

    /**
     * @param scopeMap
     * @param bindingMap
     */
    public InstanceMapImpl(final ScopeMap scopeMap, final BindingMap bindingMap)
    {
        this.scopeMap = scopeMap;
        this.instanceFactory = new InstanceFactoryImpl(bindingMap, this);
    }

    private final InstanceFactory instanceFactory;
    private final ScopeMap scopeMap;
}
