/**
 * 
 */
package cc.aileron.template.flow;

/**
 * @author aileron
 */
public class FlowEachContextDefault implements FlowEachContext<Object>
{
    @Override
    public Object call(final Object object)
    {
        idx++;
        return object;
    }

    @Override
    public Object endCall(final Object object)
    {
        idx++;
        z = true;
        return object;
    }

    @Override
    public int i()
    {
        return idx;
    }

    @Override
    public boolean z()
    {
        return z;
    }

    int idx = -1;
    boolean z;
}
