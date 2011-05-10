/**
 * 
 */
package cc.aileron.wsgi.phase;

import javax.servlet.http.HttpServletResponse;

import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.wsgi.context.WsgiContextProvider;

/**
 * ダウンロード処理実行
 * 
 * @author aileron
 */
public class DownloadPhaseExecutor implements WorkflowProcess<DownloadPhase>
{
    @Override
    public void doProcess(final WorkflowActivity<DownloadPhase> activity)
            throws Exception
    {
        final DownloadPhase resource = activity.resource();
        final HttpServletResponse response = WsgiContextProvider.context()
                .response();
        response.setHeader("Content-Type", resource.contentType());
        if (resource.filename() != null)
        {
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + new String(resource.filename().getBytes("UTF-8"),
                            "ISO8859_1") + "\"");
        }
        resource.output(response.getOutputStream());
    }
}
