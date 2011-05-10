/**
 *
 */
package cc.aileron.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class DomainTest2
{
    /**
     */
    @Test
    public void spec()
    {
        assertThat(manager.get(SampleDomain2.class, 10).value(),
                is((Object) 10));
        assertThat(manager.get(SampleDomain2.class, "test").value(),
                is((Object) "test"));
    }

    @Inject
    DomainManager manager;
}

@ImplementedByDomain(SampleDomainImpl2.class)
interface SampleDomain2
{
    Object value();
}

class SampleDomainImpl2 implements SampleDomain2
{
    interface IntConfigure extends DomainConfigure<Integer>
    {
    }

    interface StrConfigure extends DomainConfigure<String>
    {
    }

    @Override
    public Object value()
    {
        return value;
    }

    /**
     * @param configure
     */
    public SampleDomainImpl2(final IntConfigure configure)
    {
        this.value = configure.entity();
    }

    /**
     * @param configure
     */
    public SampleDomainImpl2(final StrConfigure configure)
    {
        this.value = configure.entity();
    }

    private final Object value;
}
