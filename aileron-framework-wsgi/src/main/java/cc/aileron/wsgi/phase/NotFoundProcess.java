/**
 * 
 */
package cc.aileron.wsgi.phase;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.wsgi.context.WsgiContextProvider;

import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class NotFoundProcess implements WorkflowProcess<Object>
{
    @Override
    public void doProcess(final WorkflowActivity<Object> activity)
            throws Exception
    {
        WsgiContextProvider.context().response().setStatus(404);
    }
}
