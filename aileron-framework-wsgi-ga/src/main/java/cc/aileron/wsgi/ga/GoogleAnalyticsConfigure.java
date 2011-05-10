/**
 * 
 */
package cc.aileron.wsgi.ga;

import cc.aileron.workflow.WorkflowBinder;
import cc.aileron.workflow.WorkflowConfigure;
import cc.aileron.workflow.WorkflowMethod;
import cc.aileron.workflow.phase.DisposePhaseExecutor;

/**
 * @author aileron
 */
public class GoogleAnalyticsConfigure implements WorkflowConfigure
{
    @Override
    public void configure(final WorkflowBinder binder) throws Exception
    {
        binder.bind(GoogleAnalyticsImage.class)
                .method(WorkflowMethod.GET)
                .uri(url)
                .to()
                .process(DisposePhaseExecutor.class);
    }

    /**
     * @param url
     */
    public GoogleAnalyticsConfigure(final String url)
    {
        this.url = url;
    }

    private final String url;
}
