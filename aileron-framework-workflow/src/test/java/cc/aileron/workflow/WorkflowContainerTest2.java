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
package cc.aileron.workflow;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;
import cc.aileron.workflow.container.WorkflowContainer;
import cc.aileron.workflow.container.WorkflowDto;
import cc.aileron.workflow.container.WorkflowFindCondition;
import cc.aileron.workflow.container.WorkflowRequestParameter;
import cc.aileron.workflow.util.MethodExecutor;

import com.google.inject.Inject;

/**
 * @author Aileron
 */
@RunWith(GuiceInjectRunner.class)
public class WorkflowContainerTest2
{
    /**
     * @author aileron
     */
    public static class ImageJpeg
    {
        /**
         */
        public void print()
        {
            System.out.println("jpeg[" + id + "]");
        }

        /**
         * id
         */
        public int id;
    }

    /**
     * @author aileron
     */
    public static class ImagePng
    {
        /**
         */
        public void print()
        {
            System.out.println("png[" + id + "]");
        }

        /**
         * id
         */
        public int id;
    }

    /**
     * @author aileron
     */
    public static class R1
    {
        /**
         * r3
         */
        public void print()
        {
            System.out.println("r1:" + r1);
        }

        /**
         * r1
         */
        public String r1;
    }

    /**
     * @author aileron
     */
    public static class R2
    {

        /**
         * r3
         */
        public void print()
        {
            System.out.println("r2:" + r2);
        }

        /**
         * r2
         */
        public String r2;
    }

    /**
     * @author aileron
     * 
     */
    public static class R3
    {
        /**
         * r3
         */
        public void print()
        {
            System.out.println("r3:" + r3);
        }

        /**
         * r3
         */
        public String r3;
    }

    /**
     * @author aileron
     * 
     */
    public static class R4
    {
        /**
         * r4
         */
        public void print()
        {
            System.out.println("r4:" + a + "," + b);
        }

        /**
         * a
         */
        public String a;

        /**
         * b
         */
        public String b;
    }

    /**
     * @author aileron
     * 
     */
    public static class R5
    {
        /**
         * r4
         */
        public void print()
        {
            System.out.println("r5:" + area + "," + city);
        }

        /**
         * area
         */
        public String area;

        /**
         * city
         */
        public String city;
    }

    /**
     * @author aileron
     * 
     */
    public static class R6
    {
        /**
         * r4
         */
        public void print()
        {
            System.out.println("r5:" + area + "," + city);
        }

        /**
         * area
         */
        public String area;

        /**
         * city
         */
        public String city;
    }

    /**
     * spec
     */
    @Test
    public void spec()
    {
        binder.bind(ImagePng.class)
                .method(WorkflowMethod.GET)
                .uri("/${id}.png")
                .to()
                .process(new MethodExecutor("print"));

        binder.bind(ImageJpeg.class)
                .method(WorkflowMethod.GET)
                .uri("/${id}.jpeg")
                .to()
                .process(new MethodExecutor("print"));

        binder.bind(R1.class)
                .method(WorkflowMethod.GET)
                .uri("/${r1}/1")
                .to()
                .process(new MethodExecutor("print"));

        binder.bind(R2.class)
                .method(WorkflowMethod.GET)
                .uri("/${r2}/2")
                .to()
                .process(new MethodExecutor("print"));

        binder.bind(R3.class)
                .method(WorkflowMethod.GET)
                .uri("/${r3}/3")
                .to()
                .process(new MethodExecutor("print"));

        binder.bind(R4.class)
                .method(WorkflowMethod.GET)
                .uri("/${a}/${b}/4")
                .to()
                .process(new MethodExecutor("print"));

        binder.bind(R4.class)
                .method(WorkflowMethod.GET)
                .uri("/${a}/${b}/4")
                .to()
                .process(new MethodExecutor("print"));

        binder.bind(R5.class)
                .method(WorkflowMethod.GET)
                .uri("/${area}_sitemap")
                .to()
                .process(new MethodExecutor("print"));

        binder.bind(R6.class)
                .method(WorkflowMethod.GET)
                .uri("/${city}_${area}/area_list/")
                .to()
                .process(new MethodExecutor("print"));

        execute("/hogehoge/1");
        execute("/hogehoge/2");
        execute("/hogehoge/3");

        execute("/fuga/1");
        execute("/fuga/2");
        execute("/fuga/3");

        execute("/fuga/1");
        execute("/fuga/2");
        execute("/fuga/3");

        execute("/hoge/hoge/4");
        execute("/hoge/fuga/4");
        execute("/fuga/hoge/4");

        execute("/kanazawa_ishikawa/area_list");

        execute("/1.png");
        execute("/2.png");

        execute("/10.jpeg");
    }

    /**
     * @param uri
     */
    private void execute(final String uri)
    {
        final WorkflowFindCondition find = new WorkflowFindCondition();
        find.method = WorkflowMethod.GET;
        find.uri = uri;
        find.parameter = nullParameter;

        final WorkflowDto dto = container.get(find);
        if (dto.isNull())
        {
            System.out.println("uri[" + uri + "] is null");
            return;
        }

        dto.execute();
    }

    /**
     * @param instance
     */
    @Inject
    public WorkflowContainerTest2(final InstanceManager instance)
    {
        this.container = instance.get(WorkflowContainer.class);
        this.binder = instance.get(WorkflowBinder.class);
    }

    private final WorkflowBinder binder;
    private final WorkflowContainer container;
    private final WorkflowRequestParameter nullParameter = new WorkflowRequestParameter()
    {

        @Override
        public Object get(final String name)
        {
            return null;
        }

        @Override
        public Set<String> getKeys()
        {
            return Collections.emptySet();
        }

        @Override
        public Iterator<Entry<String, Object>> iterator()
        {
            // TODO Auto-generated method stub
            return null;
        }

    };
}
