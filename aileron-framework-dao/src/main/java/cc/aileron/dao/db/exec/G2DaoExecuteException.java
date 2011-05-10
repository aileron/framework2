/**
 * 
 */
package cc.aileron.dao.db.exec;

/**
 * @author aileron
 */
public class G2DaoExecuteException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * @return sqlName
     */
    public String sqlName()
    {
        return sqlName;
    }

    /**
     * @return {@link Throwable}
     */
    public Throwable throwable()
    {
        return th;
    }

    /**
     * @param sqlName
     * @param th 
     */
    public G2DaoExecuteException(final String sqlName, final Throwable th)
    {
        this.sqlName = sqlName;
        this.th = th;
    }

    private final String sqlName;

    private final Throwable th;
}
