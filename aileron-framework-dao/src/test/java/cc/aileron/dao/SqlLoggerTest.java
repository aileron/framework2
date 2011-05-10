/**
 *
 */
package cc.aileron.dao;

import java.util.List;

import org.junit.Test;

import cc.aileron.dao.db.sql.SqlLogger;
import cc.aileron.dao.db.sql.SqlLoggerImpl;
import cc.aileron.dao.db.sql.SqlLoggerParameter;
import cc.aileron.generic.util.SkipList;

/**
 * @author aileron
 * 
 */
public class SqlLoggerTest
{
    /**
     * spec
     */
    @Test
    public void spec()
    {
        final List<Object> p = new SkipList<Object>();
        p.add("hogehoge $ hogehoge");
        logger.output(new SqlLoggerParameter("test",
                "select * from hogehoge where ?",
                p));
    }

    private final SqlLogger logger = new SqlLoggerImpl();
}
