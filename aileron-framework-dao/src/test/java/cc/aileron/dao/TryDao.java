/**
 * 
 */
package cc.aileron.dao;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.dao.e.SampleA;
import cc.aileron.dao.e.SampleB;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;
import cc.aileron.junit.runner.guice.GuiceInjectRunnerModule;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
@GuiceInjectRunnerModule(SampleModule.class)
public class TryDao
{

    /**
     * spec
     */
    @Test
    public void spec1()
    {
        final int i = dao.from(SampleB.class).insert(new SampleB()
        {
            @Override
            public String value()
            {
                return "hogehoge" + System.currentTimeMillis();
            }
        }).intValue();
        System.out.println(i);
    }

    /**
     * spec-2
     */
    @Test
    public void spec2()
    {
        final SampleB val = new SampleB()
        {
            @Override
            public String value()
            {
                return "test";
            }
        };

        final SampleA mya = dao.from(SampleA.class)
                .execute(SampleB.class)
                .value(val);

        System.out.println(mya);
    }

    /**
     * sql ファイルが無い時のテスト
     */
    @Test(expected = Error.class)
    public void spec3()
    {
        dao.from(SampleA.class).where(new SampleA()).find().list();
    }

    /**
     * トランザクションテスト
     */
    @Test(expected = NullPointerException.class)
    public void spec5()
    {
        transaction.insert();
    }

    /**
     * 普通の Select
     */
    @Test
    public void spec6()
    {
        for (final SampleA e : dao.from(SampleA.class).where().find().list())
        {
            System.out.println(e);
        }
    }

    @Inject
    private final G2DaoManager dao = null;

    @Inject
    private final SampleTransaction transaction = null;
}