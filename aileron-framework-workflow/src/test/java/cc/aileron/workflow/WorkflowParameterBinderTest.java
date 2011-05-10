/**
 * 
 */
package cc.aileron.workflow;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.generic.util.SkipList;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class WorkflowParameterBinderTest
{
    /**
     * @throws Exception
     */
    @Test
    public void spec() throws Exception
    {
        final PojoAccessor<WorkflowParameterBinderTest> accessor = manager.from(this);
        final Map<String, Object> p = new HashMap<String, Object>();
        p.put("a", new SkipList<String>("1", "10", "100", "9090"));
        p.put("c", new SkipList<String>("1", "2"));
        p.put("d", new SkipList<String>("3"));
        p.put("e", "10");
        binder.bind(accessor, p);

        assertThat(a.get(0), is(1));
        assertThat(a.get(1), is(10));
        assertThat(a.get(2), is(100));
        assertThat(a.get(3), is(9090));

        assertThat(c.get(0), is(WorkflowParameterBinderSampleCat.A));
        assertThat(c.get(1), is(WorkflowParameterBinderSampleCat.B));

        assertThat(d, is(3));
    }

    /**
     * a
     */
    public List<Integer> a;

    /**
     * cat
     */
    public List<WorkflowParameterBinderSampleCat> c = new SkipList<WorkflowParameterBinderSampleCat>();

    /**
     * d
     */
    public int d;

    /**
     * e
     */
    public int e;

    @Inject
    WorkflowParameterBinder binder;

    @Inject
    PojoAccessorManager manager;
}
