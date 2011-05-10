/**
 *
 */
package cc.aileron.workflow.phase;

/**
 * 初期化後フェーズ
 * 
 * @author aileron
 */
public interface InitializedPhase
{
    /**
     * 外部からパラメータが設定されきった際に呼ばれるメソッド
     * 
     * @throws Exception
     */
    void initialized() throws Exception;
}
