/**
 *
 */
package cc.aileron.container;

import cc.aileron.container.impl.InjectorFactoryImpl;

/**
 * @author aileron
 */
public class IOC
{
    /**
     * @param modules
     * @return {@link Injector}
     */
    public static Injector createInjector(final Module... modules)
    {
        return new InjectorFactoryImpl().createInjector(modules);
    }
}
