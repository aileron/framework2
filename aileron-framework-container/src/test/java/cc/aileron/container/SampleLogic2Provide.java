/**
 *
 */
package cc.aileron.container;

import cc.aileron.container.scope.Singleton;

/**
 * @author aileron
 */
@Singleton
public class SampleLogic2Provide implements Provider<SampleLogic2>
{
    @Override
    public SampleLogic2 get()
    {
        return new SampleLogic2()
        {
            @Override
            public int count()
            {
                return i++;
            }

            private int i = 0;
        };
    }
}
