/**
 * 
 */
package cc.aileron.accessor;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.generic.function.ConvertFunction;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class PojoAccessorInnerTest
{
    interface Sample
    {
        String a();

        ConvertFunction<String, String> str = new ConvertFunction<String, String>()
        {

            @Override
            public String convert(final String p)
            {
                return "[" + p + "]";
            }
        };
    }

    interface Sample0 extends Sample
    {

    }

    class SampleDto implements Sample
    {

        @Override
        public String a()
        {
            return "b";
        }
    }

    /**
     * @throws PojoPropertiesNotFoundException 
     * @throws PojoAccessorValueNotFoundException 
     */
    @Test
    public void spec()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {

        final String that = pojo.from(new SampleDto())
                .to("a%str")
                .value(String.class);

        Assert.assertThat(that, CoreMatchers.is("[b]"));
    }

    @Inject
    PojoAccessorManager pojo;
}
