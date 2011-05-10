/**
 * 
 */
package cc.aileron.workflow.auth;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowJudgment;

import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class WorkflowAuthExecutor implements WorkflowJudgment<WorkflowAuth>
{
    @Override
    public boolean doJudgment(final WorkflowActivity<WorkflowAuth> activity)
            throws Exception
    {
        return activity.resource().isAuth();
    }
}
