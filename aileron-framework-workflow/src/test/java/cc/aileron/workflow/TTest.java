/**
 *
 */
package cc.aileron.workflow;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author aileron
 */
public class TTest
{
    class Tree
    {
        public Map<String, Integer> get()
        {
            return map;
        }

        public Tree get(final String name)
        {
            return tree.get(name);
        }

        public void put(final String name, final Tree ch)
        {
            tree.put(name, ch);
        }

        String vername = "";
        private final Map<String, Integer> map = new HashMap<String, Integer>();
        private final Map<String, Tree> tree = new HashMap<String, Tree>();
    }

    /**
     * test
     */
    @Test
    public void test()
    {
        put("/test/a/", "get", 1);
        put("/test/b", "put", 2);

        Assert.assertEquals(1, get("/test/a", "get"));
        Assert.assertEquals(2, get("/test/b", "put"));

        put("/test/${id}", "get", 3);
        put("/test/${id}/test", "get", 4);

        Assert.assertEquals(3, get("/test/d", "get"));
        Assert.assertEquals(4, get("/test/c/test", "get"));

        Assert.assertEquals(0, get("/test/a/test", "get"));
    }

    private int get(final String uri, final String method)
    {
        final String[] uriTokens = uri.split("/");
        final int size = uriTokens.length;

        Tree tree = parent;
        int i = 0;
        while (i < size)
        {
            final String uriToken = uriTokens[i++];
            final Tree ch = tree.get(uriToken);
            if (ch != null)
            {
                tree = ch;
            }
            else if (!tree.vername.isEmpty())
            {
                tree = tree.get("*");
            }
            else
            {
                final Tree newch = new Tree();
                tree.put(uriToken, newch);
                tree = newch;
            }
        }
        final Integer val = tree.get().get(method);
        return val == null ? 0 : val;
    }

    private void put(final String uri, final String method, final int id)
    {
        final String[] uriTokens = uri.split("/");
        final int size = uriTokens.length;

        Tree tree = parent;
        int i = 0;
        while (i < size)
        {
            final String uriToken = uriTokens[i++];
            final Tree ch;
            final Matcher matcher = pattern.matcher(uriToken);
            if (matcher.find())
            {
                tree.vername = matcher.group(1);
                ch = tree.get("*");
            }
            else
            {
                ch = tree.get(uriToken);
            }

            if (ch != null)
            {
                tree = ch;
            }
            else
            {
                final Tree newch = new Tree();
                tree.put(tree.vername.isEmpty() ? uriToken : "*", newch);
                tree = newch;
            }
        }
        tree.get().put(method, id);
    }

    final Tree parent = new Tree();
    final Pattern pattern = Pattern.compile(Pattern.quote("${") + "(.*?)"
            + Pattern.quote("}"));
}
