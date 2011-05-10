/**
 *
 */
package cc.aileron.workflow.phase;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;

/**
 * データの保存フェーズを実行する
 * 
 * @author aileron
 * 
 */
public class UpdatePhaseExecutor implements WorkflowProcess<UpdatePhase>
{
    @Override
    public void doProcess(final WorkflowActivity<UpdatePhase> activity)
            throws Exception
    {
        activity.resource().update();
    }
}
