/**
 * 
 */
package cc.aileron.dao;

import java.sql.SQLException;

/**
 * トランザクション操作インタフェース
 * 
 * @author aileron
 */
public interface G2DaoTransaction
{
    /**
     * begine
     */
    void begin();

    /**
     * commit
     * 
     * @throws SQLException
     */
    void commit() throws SQLException;

    /**
     * rollback
     * 
     * @throws SQLException
     */
    void rollback() throws SQLException;
}
