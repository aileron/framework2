/**
 * 
 */
package cc.aileron.wsgi;

import cc.aileron.generic.util.Cast;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowTransition;
import cc.aileron.workflow.container.binder.WorkflowTransitionProcessFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class WsgiTransition
{
    /**
     * @param activity
     * @param path
     * @throws Exception
     */
    public void forward(final WorkflowActivity<?> activity, final String path)
            throws Exception
    {
        transition.create(WorkflowTransition.LOCALREDIRECT, path)
                .doProcess(Cast.<WorkflowActivity<Object>> cast(activity));
    }

    /**
     * @param activity
     * @param path
     * @throws Exception
     */
    public void localRedirect(final WorkflowActivity<?> activity,
            final String path) throws Exception
    {
        transition.create(WorkflowTransition.LOCALREDIRECT, path)
                .doProcess(Cast.<WorkflowActivity<Object>> cast(activity));
    }

    /**
     * @param activity
     * @param path
     * @throws Exception
     */
    public void redirect(final WorkflowActivity<?> activity, final String path)
            throws Exception
    {
        transition.create(WorkflowTransition.LOCALREDIRECT, path)
                .doProcess(Cast.<WorkflowActivity<Object>> cast(activity));
    }

    @Inject
    WorkflowTransitionProcessFactory transition;
}
