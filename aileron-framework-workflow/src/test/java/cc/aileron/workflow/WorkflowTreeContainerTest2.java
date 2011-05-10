/**
 *
 */
package cc.aileron.workflow;

import static cc.aileron.workflow.WorkflowMethod.*;

import java.util.HashMap;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.junit.runner.guice.GuiceInjectRunner;
import cc.aileron.workflow.container.tree.WorkflowTreeContainer;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class WorkflowTreeContainerTest2
{
    /**
     * test
     */
    @Test
    public void test()
    {
        t.put("/${a}_${b}_${c}/test", GET, 1);
        t.put("/${a}_${b}_${c}/test", GET, "edit", 2);
        t.put("/${a}_${b}_${c}/test", GET, "update", 3);
        t.put("/${a}_${b}_${c}/test", GET, "confirm", 4);

        as(1, "/a_b_c/test", GET, "");
        as(2, "/b_c_d/test", GET, "edit");
        as(3, "/e_f_g/test", GET, "update");
        as(4, "/h_i_j/test", GET, "confirm");

    }

    private void as(final int id, final String url,
            final WorkflowMethod method, final String key)
    {
        final HashMap<String, Object> p = new HashMap<String, Object>();
        if (!key.isEmpty())
        {
            p.put(key, null);
        }
        final Set<String> keySet = p.keySet();

        final HashMap<String, Object> up = new HashMap<String, Object>();
        Assert.assertEquals(id, t.get(url, method, up, keySet));
    }

    @Inject
    WorkflowTreeContainer t;
}
