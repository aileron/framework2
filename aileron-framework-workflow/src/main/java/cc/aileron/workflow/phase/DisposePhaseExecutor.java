/**
 * 
 */
package cc.aileron.workflow.phase;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowJudgment;
import cc.aileron.workflow.WorkflowProcess;

/**
 * 情報を受信するフェーズ
 * 
 * @author aileron
 */
public class DisposePhaseExecutor implements WorkflowJudgment<DisposePhase>,
        WorkflowProcess<DisposePhase>
{
    @Override
    public boolean doJudgment(final WorkflowActivity<DisposePhase> activity)
            throws Exception
    {
        try
        {
            activity.resource().dispose();
            return true;
        }
        catch (final DisposePhaseError e)
        {
            return false;
        }
    }

    @Override
    public void doProcess(final WorkflowActivity<DisposePhase> activity)
            throws Exception
    {
        activity.resource().dispose();
    }
}
