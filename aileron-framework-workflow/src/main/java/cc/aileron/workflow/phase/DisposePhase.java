/**
 * 
 */
package cc.aileron.workflow.phase;

/**
 * 情報を処理するフェーズ
 * 
 * @author aileron
 */
public interface DisposePhase
{
    /**
     * 処理をする
     * 
     * 処理をした際に {@link DisposePhaseError} が発生した際には
     * ジャッジメントの判断の際に false になる
     * 
     * @throws DisposePhaseError 
     * @throws Exception 
     */
    void dispose() throws DisposePhaseError, Exception;

    /**
     * 失敗
     */
    DisposePhaseError ERROR = DisposePhaseError.ERROR;
}
