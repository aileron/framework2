/**
 *
 */
package cc.aileron.workflow.phase;


/**
 * リソースの状態を検証して、不正な場合を特定する
 * 
 * @author aileron
 */
public interface ValidatePhase
{
    /**
     * @param activity
     * @return validate
     */
    boolean validate();
}
