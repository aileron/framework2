/**
 * 
 */
package cc.aileron.accessor;

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
public class PojoAccessorEnumTest
{
    /**
     * @author aileron
     */
    public static enum Category
    {
        A, B, C;
    }

    /**
     * @param name
     * @return object
     */
    public Object get(final String name)
    {
        return c;
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
        pojo.from(this).exist(Category.class.getSimpleName());
        assertThat((Category) pojo.from(this).to("test").toObject(),
                is(Category.A));
    }

    /**
     * c
     */
    public Category c = Category.A;

    @Inject
    PojoAccessorManager pojo;
}
