/**
 * 
 */
package cc.aileron.wsgi.process;

import java.io.PrintWriter;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.wsgi.context.WsgiContextProvider;

/**
 * @author aileron
 */
public class TextResponseProcess implements WorkflowProcess<Object>
{
    @Override
    public void doProcess(final WorkflowActivity<Object> activity)
            throws Exception
    {
        final PrintWriter writer = WsgiContextProvider.context()
                .response()
                .getWriter();
        writer.append(text);
    }

    /**
     * @param text 
     */
    public TextResponseProcess(final String text)
    {
        this.text = text;
    }

    private final String text;
}
