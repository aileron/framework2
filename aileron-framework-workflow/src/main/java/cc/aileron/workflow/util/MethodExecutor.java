/**
 * 
 */
package cc.aileron.workflow.util;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowJudgment;
import cc.aileron.workflow.WorkflowProcess;

/**
 * @author aileron
 */
public class MethodExecutor implements WorkflowProcess<Object>,
        WorkflowJudgment<Object>
{
    @Override
    public boolean doJudgment(final WorkflowActivity<Object> activity)
            throws Exception
    {
        final Object r = activity.resource();
        final Class<?> c = r.getClass();
        return (Boolean) c.getMethod(methodName).invoke(r);
    }

    @Override
    public void doProcess(final WorkflowActivity<Object> activity)
            throws Exception
    {
        final Object r = activity.resource();
        final Class<?> c = r.getClass();
        c.getMethod(methodName).invoke(r);
    }

    /**
     * @param methodName
     */
    public MethodExecutor(final String methodName)
    {
        this.methodName = methodName;
    }

    private final String methodName;
}
