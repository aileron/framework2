/**
 *
 */
package cc.aileron.container;

import cc.aileron.container.scope.Singleton;

/**
 * @author aileron
 */
@Singleton
public class SampleLogicImpl implements SampleLogic
{
    @Override
    public int count()
    {
        return count++;
    }

    private int count = 0;
}
