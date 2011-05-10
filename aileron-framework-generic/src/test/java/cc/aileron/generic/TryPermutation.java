/**
 * 
 */
package cc.aileron.generic;

import org.junit.Test;

import cc.aileron.generic.util.Permutation;

/**
 * @author aileron
 */
public class TryPermutation
{
    /**
     * spec
     */
    @Test
    public void spec()
    {
        final String[] a = { "a", "b", "c" };
        System.out.println(a.length);
        System.out.println(new Permutation<String>(a).choice(0));
    }

}
