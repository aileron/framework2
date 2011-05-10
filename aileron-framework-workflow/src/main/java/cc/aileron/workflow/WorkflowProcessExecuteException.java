/**
 * 
 */
package cc.aileron.workflow;

/**
 * 処理中に発生した例外
 * 
 * @author aileron
 */
public class WorkflowProcessExecuteException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * @param exception
     * @param activity
     * @param process
     */
    public WorkflowProcessExecuteException(final Exception exception,
            final WorkflowActivity<?> activity, final WorkflowProcess<?> process)
    {
        super(exception);
        this.activity = activity;
        this.process = process;
    }

    /**
     * リソースの状態
     */
    public final WorkflowActivity<?> activity;

    /**
     * 処理
     */
    public final WorkflowProcess<?> process;
}
