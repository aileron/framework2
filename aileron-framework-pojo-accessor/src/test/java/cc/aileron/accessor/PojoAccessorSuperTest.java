/**
 *
 */
package cc.aileron.accessor;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.generic.util.SkipList;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class PojoAccessorSuperTest
{
    interface ListObject extends Name
    {
        /**
         * @return names
         */
        List<ListObject> ch();

    }

    interface Name
    {
        String name();
    }

    interface Object1 extends Name
    {
        Object2 ch();
    }

    interface Object2 extends Name
    {
        Object3 ch();
    }

    interface Object3 extends Name
    {
        Object4 ch();
    }

    interface Object4 extends Name
    {
    }

    /**
     * each
     * 
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    @Test
    public void each()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final List<ListObject> ch = new SkipList<ListObject>();
        for (int i = 0; i < 10; i++)
        {
            final String name = "ch" + i;
            ch.add(new ListObject()
            {

                @Override
                public List<ListObject> ch()
                {
                    return null;
                }

                @Override
                public String name()
                {
                    return name;
                }
            });
        }

        final ListObject p3 = new ListObject()
        {

            @Override
            public List<ListObject> ch()
            {
                return ch;
            }

            @Override
            public String name()
            {
                return "p3";
            }
        };

        final ListObject p2 = new ListObject()
        {
            @Override
            public List<ListObject> ch()
            {
                return ch;
            }

            @Override
            public String name()
            {
                return "p2";
            }

            private final List<ListObject> ch = new SkipList<ListObject>(p3);
        };

        final ListObject p1 = new ListObject()
        {
            @Override
            public List<ListObject> ch()
            {
                return ch;
            }

            @Override
            public String name()
            {
                return "p1";
            }

            final List<ListObject> ch = new SkipList<ListObject>(p2);
        };

        for (final PojoAccessor<ListObject> e : pojo.from(p1)
                .to("ch")
                .accessorIterable(ListObject.class)
                .iterator()
                .next()
                .to("ch")
                .accessorIterable(ListObject.class)
                .iterator()
                .next()
                .to("ch")
                .accessorIterable(ListObject.class))
        {
            System.out.println("------------------------------");
            System.out.println(e.to("name").value(String.class));
            System.out.println(e.to("super.name").value(String.class));
            System.out.println(e.to("super.super.name").value(String.class));
            System.out.println(e.to("super.super.super.name")
                    .value(String.class));
        }
    }

    /**
     * spec
     * 
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    @Test
    public void spec()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final Object4 object4 = new Object4()
        {
            @Override
            public String name()
            {
                return "object4";
            }
        };

        final Object3 object3 = new Object3()
        {

            @Override
            public Object4 ch()
            {
                return object4;
            }

            @Override
            public String name()
            {
                return "object3";
            }
        };

        final Object2 object2 = new Object2()
        {
            @Override
            public Object3 ch()
            {
                return object3;
            }

            @Override
            public String name()
            {
                return "object2";
            }
        };

        final Object1 object1 = new Object1()
        {

            @Override
            public Object2 ch()
            {
                return object2;
            }

            @Override
            public String name()
            {
                return "object1";
            }
        };

        final PojoAccessor<Object1> accessor = pojo.from(object1);
        System.out.println("1:"
                + accessor.to("ch.ch.ch.name").value(String.class));
        System.out.println("2:"
                + accessor.to("ch.ch.ch.super.name").value(String.class));
        System.out.println("3:"
                + accessor.to("ch.ch.ch.super.super.name").value(String.class));
        System.out.println("4:"
                + accessor.to("ch.ch.ch.super.super.super.name")
                        .value(String.class));
    }

    /**
     * @param pojo
     */
    @Inject
    public PojoAccessorSuperTest(final PojoAccessorManager pojo)
    {
        this.pojo = pojo;
    }

    private final PojoAccessorManager pojo;
}
