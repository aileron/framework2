/**
 *
 */
package cc.aileron.domain;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class DomainTest
{
    /**
     */
    @Test
    public void spec()
    {
        final SampleEntityDto dto = new SampleEntityDto();
        dto.value = "100";

        final SampleDomain domain = manager.get(SampleDomain.class, dto);
        System.out.println(domain.value());
        domain.value("test");
        System.out.println(domain.value());

        manager.get(SampleDomain.class, dto);
        manager.get(SampleDomain.class, dto);
        manager.get(SampleDomain.class, dto);
        manager.get(SampleDomain.class, dto);
        manager.get(SampleDomain.class, dto);
        manager.get(SampleDomain.class, dto);

        final long i = System.nanoTime();
        new SampleDomainImpl(new DomainConfigure<SampleEntityDto>()
        {
            @Override
            public SampleEntityDto entity()
            {
                return dto;
            }

            @Override
            public <I> I instance(final Class<I> type)
            {
                return instance.get(type);
            }
        });
        System.out.println("createdAt:d:" + (System.nanoTime() - i));

        final long i2 = System.nanoTime();
        new SampleDomainImpl(new DomainConfigure<SampleEntityDto>()
        {
            @Override
            public SampleEntityDto entity()
            {
                return dto;
            }

            @Override
            public <I> I instance(final Class<I> type)
            {
                return instance.get(type);
            }
        });
        System.out.println("createdAt:d:" + (System.nanoTime() - i2));
    }

    @Inject
    final InstanceManager instance = null;

    @Inject
    DomainManager manager;
}

@ImplementedByDomain(SampleDomainImpl.class)
interface SampleDomain extends SampleEntity
{
    /**
     * @param value
     */
    void value(String value);
}

class SampleDomainImpl extends SampleEntityDto implements SampleDomain
{
    @Override
    public void value(final String value)
    {
        this.value = value;
    }

    /**
     * @param configure
     */
    public SampleDomainImpl(final DomainConfigure<SampleEntityDto> configure)
    {
        this.value = configure.entity().value;
    }
}

@ImplementedBy(SampleEntityDto.class)
interface SampleEntity
{
    /**
     * @return value
     */
    String value();
}

class SampleEntityDto implements SampleEntity
{
    @Override
    public String value()
    {
        return value;
    }

    String value;
}