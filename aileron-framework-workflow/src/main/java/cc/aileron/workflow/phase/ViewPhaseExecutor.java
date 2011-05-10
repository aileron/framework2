/**
 *
 */
package cc.aileron.workflow.phase;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;

/**
 * 情報の表示フェーズを実行する
 * 
 * @author aileron
 */
public class ViewPhaseExecutor implements WorkflowProcess<ViewPhase>
{
    @Override
    public void doProcess(final WorkflowActivity<ViewPhase> activity)
            throws Exception
    {
        activity.resource().view();
    }
}
