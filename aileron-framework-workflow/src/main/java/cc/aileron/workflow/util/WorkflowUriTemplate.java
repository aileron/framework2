/**
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
package cc.aileron.workflow.util;

import static java.util.regex.Pattern.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.generic.util.SkipList;

/**
 * @author Aileron
 * 
 */
public class WorkflowUriTemplate
{
    /**
     * pattern
     */
    static final Pattern pattern = compile(quote("${") + "(.*?)" + quote("}"));

    /**
     * @return キーの一覧
     */
    public List<String> extract()
    {
        final Matcher matcher = pattern.matcher(uriTemplate);
        final SkipList<String> list = new SkipList<String>();
        while (matcher.find())
        {
            final String key = matcher.group(1);
            list.add(key);
        }
        return list;
    }

    /**
     * @param accessor
     * @return 置き換え後文字列
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    public String replace(final PojoAccessor<?> accessor)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final Matcher matcher = pattern.matcher(uriTemplate);
        final StringBuffer buffer = new StringBuffer();
        while (matcher.find())
        {
            final String key = matcher.group(1);
            final String val = accessor.to(key).value(String.class);
            matcher.appendReplacement(buffer, val != null ? val : "");
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this).toString();
    }

    /**
     * @param uriTemplate
     */
    public WorkflowUriTemplate(final String uriTemplate)
    {
        this.uriTemplate = uriTemplate;
    }

    /**
     * uri-template
     */
    public final String uriTemplate;
}