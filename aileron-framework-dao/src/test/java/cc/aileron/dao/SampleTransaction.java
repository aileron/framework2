/**
 * 
 */
package cc.aileron.dao;

import cc.aileron.dao.db.G2Transactional;
import cc.aileron.dao.e.SampleC;

import com.google.inject.Inject;

/**
 * @author aileron
 */
public class SampleTransaction
{
    /**
     * insert
     */
    @G2Transactional
    public void insert()
    {
        dao.from(SampleC.class).insert(new SampleC()
        {

            @Override
            public int id()
            {
                return 100;
            }

            @Override
            public String value()
            {
                return "TEST-TEST";
            }
        });

        dao.from(SampleC.class).insert(new SampleC()
        {

            @Override
            public int id()
            {
                return 200;
            }

            @Override
            public String value()
            {
                return "TEST-TEST";
            }
        });

        dao.from(SampleC.class).insert(new SampleC()
        {

            @Override
            public int id()
            {
                return 300;
            }

            @Override
            public String value()
            {
                return "TEST-TEST";
            }
        });

        throw null;
    }

    /**
     * @param dao
     */
    @Inject
    public SampleTransaction(final G2DaoManager dao)
    {
        this.dao = dao;
    }

    private final G2DaoManager dao;
}
