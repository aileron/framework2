/**
 *
 */
package cc.aileron.container.impl;

import cc.aileron.container.Binder;
import cc.aileron.container.Injector;
import cc.aileron.container.Module;
import cc.aileron.container.model.BindingMap;
import cc.aileron.container.model.InjectorFactory;
import cc.aileron.container.model.ScopeMap;
import cc.aileron.container.scope.Singleton;

/**
 * @author aileron
 */
public class InjectorFactoryImpl implements InjectorFactory
{
    @Override
    public Injector createInjector(final Module... modules)
    {
        final ScopeMap scopeMap = new ScopeMapImpl();
        final BindingMap bindingMap = new BindingMapImpl(scopeMap);
        final Binder binder = new BinderImpl(bindingMap, scopeMap);
        for (final Module module : modules)
        {
            module.configure(binder);
        }
        binder.bindScope(Singleton.class, new ScopeSingleton());
        return bindingMap.getInjector();
    }

}
