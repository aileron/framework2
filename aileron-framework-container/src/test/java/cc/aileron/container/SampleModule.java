/**
 *
 */
package cc.aileron.container;

import cc.aileron.container.scope.Singleton;

/**
 * @author aileron
 */
public class SampleModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.bind(SampleLogic3.class)
                .to(SampleLogic3Impl.class)
                .in(Singleton.class);
    }
}
