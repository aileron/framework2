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
package cc.aileron.template.reader;

import static cc.aileron.template.reader.TemplateSyntaxEexception.Category.*;
import static java.util.regex.Pattern.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.template.comment.BlockComment;
import cc.aileron.template.flow.FlowCategory;
import cc.aileron.template.flow.FlowComponent;
import cc.aileron.template.flow.FlowMethodProvider;
import cc.aileron.template.flow.FlowMethodProviderMap;
import cc.aileron.template.parser.ParserMethod;
import cc.aileron.template.parser.ParserMethodNotFoundException;
import cc.aileron.template.parser.ParserMethodProviderMap;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * 実装
 * 
 * @author Aileron
 * 
 */
@Singleton
public class TemplateReaderImpl implements TemplateReader
{
    @Override
    public FlowComponent read(final CharSequence charSequence)
            throws TemplateSyntaxEexception, ParserMethodNotFoundException
    {
        FlowComponent flow = new FlowComponent(FlowCategory.SEQUENTIAL, null);
        final ParserState state = new ParserState();
        final Matcher outTagMatcher = outTagPattern.matcher(charSequence);
        while (outTagMatcher.find())
        {
            final int idx = state.idx;
            final int start = outTagMatcher.start();
            final int end = outTagMatcher.end();
            final String group = outTagMatcher.group(1);
            state.idx = end;

            logger.trace("group : {}", group);

            if (group.indexOf('#') != 0)
            {
                continue;
            }

            if (group.equals("#"))
            {
                if (state.tagStack.isEmpty())
                {
                    final ParserMethod parserMethod = map.get("plainText")
                            .get(charSequence.subSequence(idx, start)
                                    .toString()
                                    .trim(),
                                    null);
                    flow.add(parserMethod);
                    flow = flow.getParent();
                    if (flow == null)
                    {
                        throw new TemplateSyntaxEexception(NOT_OPEN,
                                getSyntaxNumberOfLines(charSequence, start),
                                charSequence);
                    }
                    continue;
                }

                final ParserTag tag = state.tagStack.pop();
                if (tag == null)
                {
                    throw new TemplateSyntaxEexception(NOT_OPEN,
                            getSyntaxNumberOfLines(charSequence, start),
                            charSequence);
                }
                final ParserMethod parserMethod = map.get(tag.methodName)
                        .get(charSequence.subSequence(idx, start)
                                .toString()
                                .trim(),
                                tag.args);
                flow.add(parserMethod);

                continue;
            }

            if (!state.isTagScope())
            {
                final ParserMethod parserMethod = map.get("plainText")
                        .get(charSequence.subSequence(idx, start).toString(),
                                null);
                flow.add(parserMethod);
            }

            final ParserTag tag = getParserTag(charSequence, group, start);
            if (tag.isFlow)
            {
                final FlowCategory category = FlowCategory.valueOf(tag.methodName.toUpperCase());
                final FlowMethodProvider provider = flowMap.get(category);
                final FlowComponent child = new FlowComponent(category,
                        provider.get(tag.args));
                flow.add(child);
                flow = child;
                continue;
            }
            if (tag.isShort)
            {
                final ParserMethod parserMethod = map.get(tag.methodName)
                        .get(null, tag.args);
                flow.add(parserMethod);
                continue;
            }
            state.tagStack.push(tag);
        }
        final ParserMethod parserMethod = map.get("plainText")
                .get(charSequence.subSequence(state.idx, charSequence.length())
                        .toString(),
                        null);
        flow.add(parserMethod);
        return flow;
    }

    /**
     * 
     * @param charSequence
     * @param group
     * @param start
     * @return
     * @throws TemplateSyntaxEexception
     */
    private ParserTag getParserTag(final CharSequence charSequence,
            final String group, final int start)
            throws TemplateSyntaxEexception
    {
        final boolean isShortTag = group.endsWith("#");
        final Matcher inTagMatcher = inTagPattern.matcher(group);
        if (!inTagMatcher.find())
        {
            throw new TemplateSyntaxEexception(METHOD_SYNTAX_ERROR,
                    getSyntaxNumberOfLines(charSequence, start),
                    charSequence);
        }
        final String source = inTagMatcher.group(1);

        logger.trace("getParserTag#source {}", source);

        if (source.length() == 0)
        {
            throw new TemplateSyntaxEexception(METHOD_SYNTAX_ERROR,
                    getSyntaxNumberOfLines(charSequence, start),
                    charSequence);
        }
        final int index;
        {
            final int tmp = source.indexOf(" ");
            index = tmp != -1 ? tmp + 1 : source.length();
        }

        final ParserTag tag = new ParserTag();
        tag.methodName = source.split(" ")[0];
        tag.args = source.substring(index, source.length());
        tag.args = tag.args == null ? "" : tag.args;
        tag.isShort = isShortTag;
        tag.isFlow = FlowCategory.isFlowStatement(tag.methodName);

        return tag;
    }

    /**
     * インディックスから行数を取得する
     * 
     * @param charSequence
     * @param idx
     * @return 行数
     */
    private int getSyntaxNumberOfLines(final CharSequence charSequence,
            final int idx)
    {
        final String text = charSequence.subSequence(0, idx)
                .toString()
                .replace(r + n, n)
                .replace(r, n);
        return text.split(n).length;
    }

    /**
     * @param factory
     * @param blockCommentType
     * @param flowMap
     * @param map
     */
    @Inject
    public TemplateReaderImpl(final InstanceManager factory,
            final BlockComment blockCommentType,
            final FlowMethodProviderMap flowMap,
            final ParserMethodProviderMap map)
    {
        this.flowMap = flowMap;
        this.map = map;

        outTagPattern = compile(quote(blockCommentType.open()) + "(#.*?)"
                + quote(blockCommentType.close()));

        inTagPattern = compile(quote("#{") + "(.*)" + quote("}") + "#?");

        logger.trace("inTagPattern  : {}", inTagPattern.pattern());
        logger.trace("outTagPattern : {}", outTagPattern.pattern());
    }

    /**
     * flow-map
     */
    private final FlowMethodProviderMap flowMap;

    /**
     * in-tag-pattern
     */
    private final Pattern inTagPattern;

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ParserMethodProviderMap map;

    /**
     * 改行コード
     */
    private final String n = "\n";
    private final Pattern outTagPattern;
    /**
     * 改行コード
     */
    private final String r = "\r";
}
