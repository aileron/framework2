/**
 *
 */
package cc.aileron.dao.db.exec;

/**
 * UPDATEの際に、対象オブジェクトと 検索条件オブジェクトをSQLにバインドする為のオブジェクト
 * 
 * @author aileron
 */
public class G2DaoObject
{
    /**
     * @param condition
     * @param target
     */
    public G2DaoObject(final Object condition, final Object target)
    {
        this.target = target;
        this.condition = condition;
    }

    /**
     * 検索条件
     */
    public final Object condition;

    /**
     * 対象オブジェクト
     */
    public final Object target;
}
