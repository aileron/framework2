/**
 *
 */
package cc.aileron.workflow.phase;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowJudgment;

/**
 * @author aileron
 */
public class ValidatePhaseExecutor implements WorkflowJudgment<ValidatePhase>
{
    @Override
    public boolean doJudgment(final WorkflowActivity<ValidatePhase> activity)
            throws Exception
    {
        return activity.resource().validate();
    }

}
