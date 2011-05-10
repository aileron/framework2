/**
 *
 */
package cc.aileron.dao.db.exec;

import com.google.inject.ImplementedBy;

/**
 * SqlFile名を特定する為のロジック
 * 
 * @author aileron
 */
@ImplementedBy(G2DaoSqlNameImpl.class)
public interface G2DaoSqlName
{
    /**
     * @param condition
     * @param type
     * @return sql-file-name
     */
    String get(Object condition, Class<?> type);
}
