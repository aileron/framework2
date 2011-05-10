/**
 * 
 */
package cc.aileron.dao.db;

import javax.sql.DataSource;

/**
 * @author aileron
 */
public interface DataSourceProvider
{
    /**
     * @return {@link DataSource}
     * @throws Exception
     */
    DataSource get() throws Exception;
}
