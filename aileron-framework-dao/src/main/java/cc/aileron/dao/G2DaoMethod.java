/**
 *
 */
package cc.aileron.dao;

/**
 * @author aileron
 */
public enum G2DaoMethod
{
    /**
     * count by
     */
    COUNT("count"),

    /**
     * execute by
     */
    EXEC("execute"),

    /**
     * find by
     */
    FIND("find");

    /**
     * @return method-name
     */
    public String sqlFileNamePrefix()
    {
        return sqlFileNamePrefix;
    }

    /**
     * @param sqlFileNamePrefix
     */
    private G2DaoMethod(final String sqlFileNamePrefix)
    {
        this.sqlFileNamePrefix = sqlFileNamePrefix;
    }

    private final String sqlFileNamePrefix;
}
