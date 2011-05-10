/**
 *
 */
package cc.aileron.workflow;

import org.junit.Test;

/**
 * @author aileron
 */
public class UriSplitTest
{
    /**
     */
    @Test
    public void test()
    {
        final String[] tokens = "/corp_10/list".split("[-_/]");

        for (final String i : tokens)
        {
            System.out.println(i);
        }
    }
}
