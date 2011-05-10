/*
 * Copyright (C) 2009 aileron.cc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.aileron.accessor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.generic.function.ConvertFunction;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.Inject;

/**
 * @author Aileron
 */
@RunWith(GuiceInjectRunner.class)
public class PojoAccessorManagerTest
{
    /**
     * @author Aileron
     */
    public static class Sample
    {
        /**
         * test
         */
        public static ConvertFunction<Sample, String> test = new ConvertFunction<Sample, String>()
        {
            @Override
            public String convert(final Sample p)
            {
                return p.t0 + p.t4 + p.t5;
            }
        };

        /**
         * test2
         */
        public static ConvertFunction<String, String> test2 = new ConvertFunction<String, String>()
        {
            @Override
            public String convert(final String p)
            {
                return "hoge";
            }
        };

        /**
         * @param t5
         */
        public void t5(final int t5)
        {
            this.t5 = t5;
        }

        /**
         * array
         */
        public String[] array = { "aaa", "bbb", "ccc" };

        /**
         * eh-map
         */
        public HashMap<SampleEnum, Integer> ehmap = new HashMap<PojoAccessorManagerTest.SampleEnum, Integer>();

        /**
         * emap
         */
        public EnumMap<SampleEnum, Integer> emap = new EnumMap<PojoAccessorManagerTest.SampleEnum, Integer>(SampleEnum.class);

        /**
         * list
         */
        public List<Integer> list = new ArrayList<Integer>();

        /**
         * map
         */
        public Map<String, Integer> map = new HashMap<String, Integer>();

        /**
         * value
         */
        public String t0;

        /**
         * t1
         */
        public T t1 = new T();

        /**
         * t2
         */
        public T t2;

        /**
         * t3
         */
        public boolean t3;

        /**
         * t4
         */
        public int t4;

        /**
         * t5
         */
        public int t5;
    }

    /**
     * enum
     * 
     * @author aileron
     * 
     */
    public static enum SampleEnum
    {
        A, B, C
    }

    /**
     * @author Aileron
     * 
     */
    public static class T
    {
        /**
         * default constractor
         */
        @Inject
        public T()
        {
        }

        /**
         * value
         */
        public String value = "default";
    }

    /**
     * booleancheck
     * 
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    @Test
    public void booleanchek()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final Sample sample = new Sample();

        sample.t4 = 10;

        final Boolean check = accessor.from(sample)
                .to("t4")
                .value(Boolean.class);
        assertThat(check, is(true));

        sample.t4 = 0;
        final Boolean checkf = accessor.from(sample)
                .to("t4")
                .value(Boolean.class);
        assertThat(checkf, is(false));

        assertThat(!checkf, is(true));
        assertThat(!check, is(false));
    }

    /**
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    @Test
    public void convert()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final PojoAccessor<Sample> self = accessor.from(Sample.class);
        self.toTarget().t0 = "test";
        self.toTarget().t4 = 100;
        self.toTarget().t5 = 200;

        final String val = self.to("self%test").value(String.class);
        assertThat(val, is("test" + 100 + 200));

        assertThat(self.to("t0%test2").value(String.class), is("hoge"));
    }

    /**
     * Listインタフェースに対しての操作
     * 
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    @Test
    public void getindex()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        assertThat(Number.class.isAssignableFrom(Integer.class), is(true));

        final Sample sample = new Sample();
        sample.list.add(100);
        sample.list.add(200);
        sample.list.add(300);

        sample.map.put("0", 100);
        sample.map.put("1", 200);
        sample.map.put("test", 300);

        assertThat(accessor.from(sample).to("list.0").value(Integer.class),
                is(100));
        assertThat(accessor.from(sample).to("list.1").value(Integer.class),
                is(200));
        assertThat(accessor.from(sample).to("list.2").value(Integer.class),
                is(300));

        sample.emap.put(SampleEnum.A, 400);
        sample.emap.put(SampleEnum.B, 500);
        sample.emap.put(SampleEnum.C, 600);

        sample.ehmap.put(SampleEnum.A, 700);

        assertThat(accessor.from(sample).to("list.0").value(Integer.class),
                is(100));
        assertThat(accessor.from(sample).to("list.1").value(Integer.class),
                is(200));
        assertThat(accessor.from(sample).to("list.2").value(Integer.class),
                is(300));

        assertThat(accessor.from(sample).to("emap.A").value(Integer.class),
                is(400));

        assertThat(accessor.from(sample).to("emap.B").value(Integer.class),
                is(500));

        assertThat(accessor.from(sample).to("emap.C").value(Integer.class),
                is(600));

        assertThat(accessor.from(sample).to("ehmap.A").value(Integer.class),
                is(700));
    }

    /**
     * @throws PojoAccessorValueNotFoundException
     * @throws PojoPropertiesNotFoundException
     */
    @Test
    public void spec()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final Sample sample = new Sample();

        accessor.from(sample).to("t0").value("test");

        assertThat(accessor.from(sample).to("t0").value(String.class),
                is("test"));

        System.out.println(accessor.from(sample)
                .to("t1.value")
                .value(String.class));

        accessor.from(sample).to("t1.value").value("test-01");

        assertThat(accessor.from(sample).to("t1.value").value(String.class),
                is("test-01"));

        accessor.from(sample).to("t2.value").value("test-02");

        assertThat(accessor.from(sample).to("t2.value").value(String.class),
                is("test-02"));

        assertThat(accessor.from(sample).to("t3").value(Boolean.class),
                is(false));

        assertThat(accessor.from(sample).to("t4").value(Boolean.class),
                is(false));

        final PojoAccessor<?> p = accessor.from(sample).mixin(new Object()
        {
            /**
             * mix
             */
            @SuppressWarnings("unused")
            public String mix = "test-mix";
        });

        assertThat(p.to("mix").value(String.class), is("test-mix"));

        p.to("t5").value("10");

        assertThat(p.to("t5").value(Integer.class), is(10));
    }

    /**
     *
     */
    @Test
    public void specKey()
    {
        final Sample sample = new Sample();

        final List<String> getkeys = accessor.from(sample)
                .keys(PojoAccessorMethod.GET);

        System.out.println(getkeys);

        final List<String> setkeys = accessor.from(sample)
                .keys(PojoAccessorMethod.SET);

        System.out.println(setkeys);
    }

    /**
     * @throws PojoAccessorValueNotFoundException
     * @throws PojoPropertiesNotFoundException
     */
    @Test
    public void ドット連結によるアクセス()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final Sample sample = new Sample();
        accessor.from(sample).to("t1.value").value("test-01");

        assertThat(sample.t1.value, is("test-01"));

        System.out.println(sample.t1.value);

        final String cmp = accessor.from(sample)
                .to("t1.value")
                .value(String.class);

        System.out.println(cmp);

        System.out.println(sample.t1.value);
    }

    /**
     * @throws PojoPropertiesNotFoundException 
     * @throws PojoAccessorValueNotFoundException 
     * 
     */
    @Test
    public void 空文字テスト()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final PojoAccessor<Sample> sample = accessor.from(new Sample());

        sample.toTarget().t0 = "test";
        System.out.println(sample.to("t0").value(Boolean.class));

        sample.toTarget().t0 = "";
        System.out.println(sample.to("t0").value(Boolean.class));

        sample.toTarget().t0 = null;
        System.out.println(sample.to("t0").value(Boolean.class));
    }

    /**
     * @throws PojoPropertiesNotFoundException 
     * @throws PojoAccessorValueNotFoundException 
     */
    @Test
    public void 配列テスト()
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        assertThat(accessor.from(Sample.class)
                .to("array.0")
                .value(String.class), is("aaa"));

        assertThat(accessor.from(Sample.class)
                .to("array.1")
                .value(String.class), is("bbb"));

        assertThat(accessor.from(Sample.class)
                .to("array.2")
                .value(String.class), is("ccc"));
    }

    /**
     * @param accessor
     */
    @Inject
    public PojoAccessorManagerTest(final PojoAccessorManager accessor)
    {
        this.accessor = accessor;
    }

    private final PojoAccessorManager accessor;
}
