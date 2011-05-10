/**
 * 
 */
package cc.aileron.workflow.phase;

/**
 * @author aileron
 */
public class DisposePhaseError extends Exception
{
    /**
     * インスタンス
     */
    public static final DisposePhaseError ERROR = new DisposePhaseError();

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    private DisposePhaseError()
    {
    }
}
