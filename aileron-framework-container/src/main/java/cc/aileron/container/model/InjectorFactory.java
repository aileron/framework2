/**
 *
 */
package cc.aileron.container.model;

import cc.aileron.container.Inject;
import cc.aileron.container.Injector;
import cc.aileron.container.Module;

/**
 * @author aileron
 */
public interface InjectorFactory
{
    /**
     * @param modules
     * @return {@link Inject}
     */
    Injector createInjector(Module... modules);
}
