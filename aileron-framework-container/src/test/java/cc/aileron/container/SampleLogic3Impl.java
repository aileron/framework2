/**
 *
 */
package cc.aileron.container;

/**
 * @author aileron
 * 
 */
public class SampleLogic3Impl implements SampleLogic3
{
    @Override
    public int count()
    {
        return i++;
    }

    private int i = 0;

}
