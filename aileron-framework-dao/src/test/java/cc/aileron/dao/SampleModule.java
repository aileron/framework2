/**
 * 
 */
package cc.aileron.dao;

import java.util.Properties;

import cc.aileron.dao.db.G2DaoConnectionProvider;
import cc.aileron.dao.e.SampleA;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * @author aileron
 *
 */
public class SampleModule implements Module
{
    @Override
    public void configure(final Binder binder)
    {
        binder.install(db);
    }

    /**
     * @throws Exception    
     */
    public SampleModule() throws Exception
    {
        /*
         * final Properties mysqlproperties = new Properties();
         * mysqlproperties.setProperty("driverClassName",
         * "com.mysql.jdbc.Driver"); mysqlproperties.setProperty("url",
         * "jdbc:mysql://localhost:3306/test?zeroDateTimeBehavior=convertToNull&autoReconnect=true&allowMultiQueries=true"
         * ); mysqlproperties.setProperty("username", "test");
         * mysqlproperties.setProperty("password", "test");
         */

        final Properties h2properties = new Properties();
        h2properties.setProperty("driverClassName", "org.h2.Driver");
        h2properties.setProperty("url",
                "jdbc:h2:/tmp/test-g2-dao;AUTO_SERVER=TRUE");
        h2properties.setProperty("username", "sa");
        h2properties.setProperty("password", "sa");

        db = new G2MDaoModule(SampleA.class.getPackage(),
                new G2DaoNetworkSetting(new G2DaoConnectionProvider(G2DaoDataSource.class,
                        h2properties)));

    }

    private final G2MDaoModule db;
}
