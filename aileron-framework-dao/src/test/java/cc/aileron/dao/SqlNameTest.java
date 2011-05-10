/**
 *
 */
package cc.aileron.dao;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.dao.db.exec.G2DaoSqlName;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class SqlNameTest
{
    /**
     * spec
     */
    @Test
    public void spec()
    {
        class A
        {
        }

        final String name = sqlName.get(new A(), null);
        System.out.println(name);
    }

    @Inject
    G2DaoSqlName sqlName;
}
