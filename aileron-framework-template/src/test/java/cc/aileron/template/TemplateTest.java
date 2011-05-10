/**
 * Copyright (C) 2008 aileron.cc
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
package cc.aileron.template;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorManager;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.commons.util.ResourceUtils;
import cc.aileron.generic.function.ConvertFunction;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;
import cc.aileron.junit.runner.guice.GuiceInjectRunnerModule;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.reader.TemplateSyntaxEexception;

import com.google.inject.Inject;

/**
 * @author Aileron
 * 
 */
@RunWith(GuiceInjectRunner.class)
@GuiceInjectRunnerModule(XmlSampleModule.class)
public class TemplateTest
{
    /**
     * @author Aileron
     */
    public static enum Category
    {
        /**
         * A
         */
        A

        /**
         * B
         */
        , B

        /**
         * C
         */
        , C

        /**
         * あいう
         */
        , あいう;

        /**
         * select-map
         */
        public static ConvertFunction<Category, Map<Category, Boolean>> selectMap = new ConvertFunction<Category, Map<Category, Boolean>>()
        {
            @Override
            public Map<Category, Boolean> convert(final Category p)
            {
                final Map<Category, Boolean> map = new HashMap<TemplateTest.Category, Boolean>();
                for (final Category c : values())
                {
                    map.put(c, false);
                }
                map.put(p, true);
                return map;
            }
        };

        /**
         * @return type
         */
        public String type()
        {
            return "test";
        }
    }

    /**
     * @author Aileron
     */
    public class Sample
    {
        public int a = 10;

        /**
         * aaa
         */
        public String aaa = "bbb";

        public Integer b = 10;

        public float c = 30.5f;

        /**
         * category
         */
        public Category category = Category.A;

        public Float d = 30.5f;

        /**
         * emap
         */
        public EnumMap<Category, Boolean> emap = new EnumMap<Category, Boolean>(Category.class);
        /**
         * is
         */
        public boolean is = false;

        /**
         * list
         */
        public List<String> list = new ArrayList<String>();
        /**
         * nulllist
         */
        public List<String> nulllist;
        /**
         * samples
         */
        public List<SampleOne> samples = new ArrayList<SampleOne>();

        /**
         * test
         */
        public String test = "sample";

        {
            emap.put(Category.A, true);
            emap.put(Category.B, false);
            emap.put(Category.あいう, true);
        }
    }

    /**
     * @author aileron
     */
    public static interface SampleOne
    {
        /**
         * test
         */
        public static final ConvertFunction<SampleOne, String> test = new ConvertFunction<SampleOne, String>()
        {
            @Override
            public String convert(final SampleOne p)
            {
                return "hoge";
            }
        };

        /**
         * @return ch
         */
        List<SampleOne> ch();

        /**
         * @return val
         */
        String val();

        /**
         * category
         */
        public final Category category = Category.A;
    }

    /**
     * @throws ResourceNotFoundException
     * @throws ParserMethodNotFoundException
     * @throws TemplateSyntaxEexception
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    @Test
    public void spec()
            throws TemplateSyntaxEexception, ParserMethodNotFoundException,
            ResourceNotFoundException, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final Template template = compiler.compile(ResourceUtils.resource("test.html")
                .toString());

        System.out.println(accessor.to("is").value(Boolean.class));

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);

        final long t = System.nanoTime();
        template.print(writer, accessor);
        System.out.println("-------------time----------"
                + (System.nanoTime() - t));

        System.out.println(stringWriter);
    }

    /**
     * @throws ResourceNotFoundException
     * @throws ParserMethodNotFoundException
     * @throws TemplateSyntaxEexception
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    @Test
    public void spec2()
            throws TemplateSyntaxEexception, ParserMethodNotFoundException,
            ResourceNotFoundException, PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final Template template = compiler.compile(ResourceUtils.resource("test2.html")
                .toString());

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);
        template.print(writer, accessor);

        System.out.println(stringWriter);
    }

    /**
     * @return sample
     */
    private Sample init()
    {
        final Sample sample = new Sample();
        sample.list.add("test1");
        sample.list.add("test2");
        sample.list.add("test3");
        sample.list.add("test4");

        for (int i = 0; i < 10; i++)
        {
            final int idx1 = i;
            sample.samples.add(new SampleOne()
            {
                @Override
                public List<SampleOne> ch()
                {
                    return ch;
                }

                @Override
                public String val()
                {
                    return "aaa" + idx1;
                }

                final List<SampleOne> ch = new ArrayList<SampleOne>();
                {
                    final SampleOne p = this;
                    for (int i = 0; i < 10; i++)
                    {
                        final int idx = i;
                        ch.add(new SampleOne()
                        {
                            @Override
                            public List<SampleOne> ch()
                            {
                                return null;
                            }

                            @Override
                            public String val()
                            {
                                return p.val() + idx;
                            }
                        });
                    }
                }
            });
        }

        return sample;
    }

    /**
     * @param manager
     * @param compiler
     */
    @Inject
    public TemplateTest(final PojoAccessorManager manager,
            final TemplateCompiler compiler)
    {
        this.compiler = compiler;
        accessor = manager.from(init());
    }

    private final PojoAccessor<Sample> accessor;

    private final TemplateCompiler compiler;
}
