/**
 *
 */
package cc.aileron.dao;

/**
 * ページング
 * 
 * @author aileron
 */
public interface G2DaoPagingContext extends G2DaoPaging
{
    /**
     * @return データ件数
     */
    int count();

    /**
     * @param count
     *            データ件数
     */
    void count(int count);
}
