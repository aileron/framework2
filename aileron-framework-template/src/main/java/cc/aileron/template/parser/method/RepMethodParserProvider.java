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
package cc.aileron.template.parser.method;

import static java.util.regex.Pattern.*;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.accessor.PojoAccessorValueNotFoundException;
import cc.aileron.accessor.PojoPropertiesNotFoundException;
import cc.aileron.generic.util.SkipList;
import cc.aileron.template.context.TemplateContext;
import cc.aileron.template.parser.ParserMethod;
import cc.aileron.template.parser.ParserMethodProvider;

import com.google.inject.Singleton;

/**
 * @author Aileron
 */
@Singleton
public class RepMethodParserProvider implements ParserMethodProvider
{
    @Override
    public ParserMethod get(final String content, final String args)
    {
        final List<RepKeyAndValue> list = new SkipList<RepKeyAndValue>();
        for (final RepKeyAndValue rep : parseArgs(args))
        {
            list.add(rep);
        }
        return new ParserMethod()
        {
            @Override
            public void call(final TemplateContext context)
                    throws PojoAccessorValueNotFoundException,
                    PojoPropertiesNotFoundException
            {
                String repContent = content;
                for (final RepKeyAndValue rep : list)
                {
                    repContent = rep(rep, repContent, context.getAccessor());
                }
                context.getNameSpace().append(repContent);
            }
        };
    }

    /**
     * @param rep
     * @param content
     * @param accessor
     * @return 置き換え後文字列
     * @throws PojoPropertiesNotFoundException
     * @throws PojoAccessorValueNotFoundException
     */
    String rep(final RepKeyAndValue rep, final String content,
            final PojoAccessor<?> accessor)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final StringBuffer sb = new StringBuffer();
        final Matcher matcher = rep.pattern(accessor).matcher(content);
        while (matcher.find())
        {
            final CharSequence start = content.subSequence(matcher.start(),
                    matcher.start(1));
            final String value = new VariableExpand(accessor).expand(rep.key);
            final CharSequence end = content.subSequence(matcher.end(1),
                    matcher.end());
            logger.trace("rep#value : {}", value);
            matcher.appendReplacement(sb, start + value + end);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * @param args
     * @return args
     */
    private Iterable<RepKeyAndValue> parseArgs(final String args)
    {
        final int size = args.length();
        return new Iterable<RepKeyAndValue>()
        {
            @Override
            public Iterator<RepKeyAndValue> iterator()
            {
                return new Iterator<RepKeyAndValue>()
                {
                    @Override
                    public boolean hasNext()
                    {
                        return idx < size;
                    }

                    @Override
                    public RepKeyAndValue next()
                    {
                        final String val = args.substring(idx).trim();
                        final Matcher matcher = pattern.matcher(val);
                        if (!matcher.find())
                        {
                            idx = size;
                            return null;
                        }

                        final int start = matcher.start();
                        final int end1, end2, vsize;
                        if (matcher.group(2) == null)
                        {
                            end1 = matcher.end();
                            end2 = val.length();
                            vsize = end2 + 1;
                        }
                        else
                        {
                            end1 = matcher.end(1) + 1;
                            end2 = matcher.end(2) - 1;
                            vsize = end2 + 2;
                        }
                        final String key = matcher.group(1);
                        final String pattern = quote(val.substring(0, start)
                                .trim())
                                + "(.*?)"
                                + quote(val.substring(end1, end2).trim());

                        final RepKeyAndValue rep = new RepKeyAndValue(key,
                                pattern.trim());
                        idx += vsize;
                        return rep;
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }

                    int idx = 0;
                    final Pattern pattern = compile(quote("${") + "(.*?)"
                            + quote("}") + "(.*? &)?");
                };
            }
        };
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
}

class RepKeyAndValue
{
    static final Pattern repPattern = Pattern.compile("\\*\\{(.*?)\\}");

    /**
     * @param accessor
     * @return {@link Pattern}
     * @throws PojoPropertiesNotFoundException 
     * @throws PojoAccessorValueNotFoundException 
     */
    public Pattern pattern(final PojoAccessor<?> accessor)
            throws PojoAccessorValueNotFoundException,
            PojoPropertiesNotFoundException
    {
        final StringBuffer buffer = new StringBuffer();
        final Matcher ma = repPattern.matcher(pattern);
        while (ma.find())
        {
            ma.appendReplacement(buffer,
                    accessor.to(ma.group(1)).value(String.class));
        }
        return Pattern.compile(ma.appendTail(buffer).toString(),
                Pattern.CASE_INSENSITIVE);
    }

    RepKeyAndValue(final String key, final String pattern)
    {
        this.key = key;
        this.pattern = pattern;
    }

    final String key;
    final String pattern;
}