/**
 * 
 */
package cc.aileron.php;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class Pojo2PhpTest
{
    enum Cate
    {
        A, B, C
    }

    @Test
    public void spec()
    {
        final String php = c.convert(new Object()
        {
            /**
             * a
             */
            public String a = "test";

            /**
             * b
             */
            public String b = "hogehoge";

            /**
             * c
             */
            public Object c = new Object()
            {
                /**
                 * @return toNumber
                 */
                public Number toNumber()
                {
                    return 10;
                }

                /**
                 * d
                 */
                public int d = 1111;

                /**
                 * e
                 */
                public int e = 2222;
            };

            /**
             * cate
             */
            public Cate cate = Cate.A;

            /**
             * f
             */
            public int f = 10;
        });

        System.out.println(php);
    }

    @Inject
    Pojo2PhpSource c;
}
