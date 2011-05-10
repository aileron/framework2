/**
 *
 */
package cc.aileron.workflow;

import java.util.ArrayList;
import java.util.Collections;
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
public class WorkflowTreeContainerTest
{
    /**
     * test
     */
    @Test
    public void test()
    {

        t.put("/test/a/", WorkflowMethod.GET, 1);
        t.put("/test/b", WorkflowMethod.GET, 2);

        Assert.assertEquals(1, t.get("/test/a", WorkflowMethod.GET, null, null));
        Assert.assertEquals(2, t.get("/test/b", WorkflowMethod.GET, null, null));

        t.put("/test/${id}", WorkflowMethod.GET, 3);
        t.put("/test/${id}/test", WorkflowMethod.GET, 4);

        t.put("/${id}/test", WorkflowMethod.GET, 5);

        t.put("/${id}/${dir}", WorkflowMethod.GET, 6);

        t.put("/search_${id}", WorkflowMethod.GET, 7);

        t.put("/line_${id}", WorkflowMethod.GET, 8);
        t.put("/area_${id}", WorkflowMethod.GET, 9);

        t.put("/${city}_${pref}/area_list", WorkflowMethod.GET, 10);
        t.put("/${city}_${pref}/line_list", WorkflowMethod.GET, 11);
        t.put("/${city}_${pref}/station_list", WorkflowMethod.GET, 12);

        t.put("/corp_${id}/list", WorkflowMethod.GET, 13);

        t.put("/${id}/detail", WorkflowMethod.GET, 14);
        t.put("/${id}/image_list", WorkflowMethod.GET, 15);
        t.put("/${id}/image_${imagenumber}", WorkflowMethod.GET, 16);

        t.put("/${city}_sitemap", WorkflowMethod.GET, 17);
        t.put("/${city}_sitemap2", WorkflowMethod.GET, 18);
        t.put("/${city}_sitemap3", WorkflowMethod.GET, 19);

        t.put("/${city}_sitemap/selection", WorkflowMethod.GET, 20);

        t.put("/${option}_${city}_${pref}/area_list", WorkflowMethod.GET, 21);
        t.put("/${option}_${city}_${pref}/line_list", WorkflowMethod.GET, 22);
        t.put("/${option}_${city}_${pref}/station_list", WorkflowMethod.GET, 23);

        final HashMap<String, Object> p = new HashMap<String, Object>();
        final Set<String> key = p.keySet();

        final HashMap<String, Object> up = new HashMap<String, Object>();

        Assert.assertEquals(3, t.get("/test/d", WorkflowMethod.GET, up, key));
        Assert.assertEquals(4,
                t.get("/test/c/test", WorkflowMethod.GET, up, key));
        Assert.assertEquals(0,
                t.get("/test/a/test", WorkflowMethod.GET, up, key));

        Assert.assertEquals(5, t.get("/bbb/test", WorkflowMethod.GET, up, key));
        Assert.assertEquals(6, t.get("/bbb/aaa", WorkflowMethod.GET, up, key));

        Assert.assertEquals(7,
                t.get("/search_tokyo", WorkflowMethod.GET, up, key));
        Assert.assertEquals(8,
                t.get("/line_tokyo", WorkflowMethod.GET, up, key));

        Assert.assertEquals(9,
                t.get("/area_tokyo", WorkflowMethod.GET, up, key));

        Assert.assertEquals(10,
                t.get("/shibuya_tokyo/area_list", WorkflowMethod.GET, up, key));

        Assert.assertEquals(11,
                t.get("/shibuya_tokyo/line_list", WorkflowMethod.GET, up, key));

        Assert.assertEquals(12, t.get("/shibuya_tokyo/station_list",
                WorkflowMethod.GET,
                up,
                key));

        Assert.assertEquals(13,
                t.get("/corp_10/list", WorkflowMethod.GET, up, key));

        final ArrayList<String> urls = new ArrayList<String>(t.all().keySet());
        Collections.sort(urls);
        for (final String e : urls)
        {
            System.out.println(e);
        }
    }

    @Inject
    WorkflowTreeContainer t;
}
