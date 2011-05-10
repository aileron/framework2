/**
 * 
 */
package cc.aileron.workflow;

/**
 * パラメータをオブジェクトにバインドしている最中の例外
 * 
 * @author aileron
 */
public class WorkflowParameterBindException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * @param exception
     * @param activity
     */
    public WorkflowParameterBindException(final Exception exception,
            final WorkflowActivity<?> activity)
    {
        super(exception);
        this.activity = activity;
    }

    /**
     * リソースの状態
     */
    public final WorkflowActivity<?> activity;
}
