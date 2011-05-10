/**
 *
 */
package cc.aileron.workflow.phase;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;

/**
 * @author aileron
 */
public class DeletePhaseExecutor implements WorkflowProcess<DeletePhase>
{
    @Override
    public void doProcess(final WorkflowActivity<DeletePhase> activity)
            throws Exception
    {
        activity.resource().delete();
    }
}
