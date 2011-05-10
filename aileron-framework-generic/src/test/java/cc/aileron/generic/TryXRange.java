/**
 * 
 */
package cc.aileron.generic;

import java.util.Iterator;

import org.junit.Test;

import cc.aileron.generic.util.XRange;

/**
 * @author aileron
 */
public class TryXRange
{
    /**
     * spec
     */
    @Test
    public void spec()
    {
        final XRange fromTo = XRange.fromTo(1, 47);

        System.out.println("------------------");
        final Iterator<Integer> ite = fromTo.iterator();
        while (ite.hasNext())
        {
            System.out.println(ite.next());
        }

        System.out.println("------------------");
        for (final int i : fromTo)
        {
            System.out.println(i);
        }

        System.out.println("------------------");
        for (final int i : fromTo)
        {
            System.out.println(i);
        }

        System.out.println("------------------");
        for (final int i : XRange.xrange(100, 0, -2))
        {
            System.out.println(i);
        }
    }
}
