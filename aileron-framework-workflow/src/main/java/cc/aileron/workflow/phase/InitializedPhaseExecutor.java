/**
 *
 */
package cc.aileron.workflow.phase;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.Singleton;

/**
 * 初期化後フェーズを実行する為の処理
 * 
 * @author aileron
 */
@Singleton
public class InitializedPhaseExecutor implements
        WorkflowProcess<InitializedPhase>
{
    @Override
    public void doProcess(final WorkflowActivity<InitializedPhase> activity)
            throws Exception
    {
        activity.resource().initialized();
    }
}
