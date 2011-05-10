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
package cc.aileron.generic.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字列の形式変換
 * 
 * @author Aileron
 */
public class StringConvertor
{
    /**
     * キャメルケース形式の文字列
     * 
     * @author Aileron
     */
    public static interface CamelCase
    {
        /**
         * @return アンダスコア形式の文字列
         */
        String toUnderScore();
    }

    /**
     * アンダスコア形式の文字列
     * 
     * @author Aileron
     */
    public static interface UnderScore
    {
        /**
         * @return キャメルケース形式の文字列
         */
        String toCamelCase();
    }

    /**
     * Implement
     * 
     * @author Aileron
     * 
     */
    private static class Implement implements CamelCase, UnderScore
    {
        /**
         * @return camelCase
         */
        @Override
        public String toCamelCase()
        {
            final StringBuffer sb = new StringBuffer();
            final Matcher matcher = pattern.matcher(value);
            for (int cnt = 0; matcher.find(); cnt++)
            {
                final String replacement = matcher.group(1).toUpperCase();
                matcher.appendReplacement(sb, replacement);
            }
            matcher.appendTail(sb);
            return sb.toString();
        }

        /**
         * @return toUnderScore
         */
        @Override
        public String toUnderScore()
        {
            final StringBuffer sb = new StringBuffer();
            final Matcher matcher = pattern.matcher(value);
            for (int cnt = 0; matcher.find(); cnt++)
            {
                final String replacement = matcher.group(1).toLowerCase();
                matcher.appendReplacement(sb, (cnt == 0 ? "" : "_")
                        + replacement);
            }
            matcher.appendTail(sb);
            return sb.toString();
        }

        /**
         * @param value
         */
        Implement(final String value, final Pattern pattern)
        {
            this.value = value;
            this.pattern = pattern;
        }

        /**
         * pattern
         */
        private final Pattern pattern;

        /**
         * value
         */
        private final String value;
    }

    /**
     * camelCasePattern
     */
    private static final Pattern camelCasePattern = Pattern.compile("(\\p{Upper})");

    /**
     * underScorePattern
     */
    private static final Pattern underScorePattern = Pattern.compile("_(.)");

    /**
     * @param value
     * @return this
     */
    public static CamelCase camelCase(final String value)
    {
        return new Implement(value, camelCasePattern);
    }

    /**
     * @param value
     * @return this
     */
    public static UnderScore underScore(final String value)
    {
        return new Implement(value, underScorePattern);
    }

}
