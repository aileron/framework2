/**
 *
 */
package cc.aileron.commons.util;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cc.aileron.generic.Procedure;
import cc.aileron.generic.function.Each;

/**
 * @author aileron
 */
public class CollectionFunctionsTest
{

    /**
     */
    @Test
    public void each()
    {
        final List<String> list = Arrays.asList("a", "b", "c");

        new Each<String>(list)
        {
            @Override
            public void call(final String t)
            {
                System.out.println(t);
            }
        }.call();

        new Each<String>()
        {
            @Override
            public void call(final String t)
            {
                System.out.println(t);
            }
        }.call(list);

        Each.each(list).apply(new Procedure<String>()
        {
            @Override
            public void call(final String t)
            {
                System.out.println(t);
            }
        });

        Each.each(new Procedure<String>()
        {
            @Override
            public void call(final String t)
            {
                System.out.println(t);
            }
        }).call(list);
    }
}
