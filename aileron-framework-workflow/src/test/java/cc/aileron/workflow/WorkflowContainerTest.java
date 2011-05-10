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

import com.google.inject.Inject;

/**
 * @author Aileron
 */
@RunWith(GuiceInjectRunner.class)
public class WorkflowContainerTest
{
    /**
     * @author Aileron
     */
    public static class SampleProcess implements
            WorkflowProcess<SampleResource>
    {
        @Override
        public void doProcess(final WorkflowActivity<SampleResource> activity)
                throws Exception
        {
            System.out.println(String.format("%1$s:[%2$s][%3$s]",
                    getClass().getSimpleName(),
                    activity.resource().dir,
                    activity.resource().id));
        }
    }

    /**
     * @author Aileron
     */
    public static class SampleProcess1 extends SampleProcess
    {
    }

    /**
     * @author Aileron
     */
    public static class SampleProcess2 extends SampleProcess
    {
    }

    /**
     * @author Aileron
     */
    public static class SampleProcess3 extends SampleProcess
    {
    }

    /**
     * @author Aileron
     */
    public static class SampleProcess4 extends SampleProcess
    {
    }

    /**
     * @author Aileron
     */
    public static class SampleProcess5 extends SampleProcess
    {
    }

    /**
     * @author Aileron
     */
    public static class SampleProcess6 extends SampleProcess
    {
    }

    /**
     * @author Aileron
     */
    public static class SampleResource
    {
        /**
         * dir
         */
        public String dir;

        /**
         * id
         */
        public String id;
    }

    /**
     * spec
     */
    @Test
    public void spec()
    {

        binder.bind(SampleResource.class)
                .uri("/${dir}/${id}/")
                .method(WorkflowMethod.GET)
                .to()
                .process(SampleProcess5.class);

        binder.bind(SampleResource.class)
                .uri("/")
                .method(WorkflowMethod.GET)
                .to()
                .process(SampleProcess1.class);

        binder.bind(SampleResource.class)
                .uri("/link/${dir}/")
                .method(WorkflowMethod.GET)
                .to()
                .process(SampleProcess6.class);

        binder.bind(SampleResource.class)
                .uri("/${dir}/content.xml")
                .method(WorkflowMethod.GET)
                .to()
                .process(SampleProcess4.class);

        binder.bind(SampleResource.class)
                .uri("/${dir}/${id}.xml")
                .method(WorkflowMethod.GET)
                .to()
                .process(SampleProcess2.class);

        binder.bind(SampleResource.class)
                .uri("/${dir}/${id}.html")
                .method(WorkflowMethod.GET)
                .to()
                .process(SampleProcess2.class);

        execute("/");
        execute("/test.html");
        execute("/test.xml");
        execute("/test/content.xml");
        execute("/test/pokapontasu.html");
        execute("/test/content.html");
        execute("/test/a.xml");
        execute("/test/hogehoge/");
        execute("/link/1");

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
    public WorkflowContainerTest(final InstanceManager instance)
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
