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
package cc.aileron.commons.util;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author Aileron
 */
public class HtmlUtils
{
    /**
     * @param value
     * @return value
     */
    public static String escape(final String value)
    {
        return StringEscapeUtils.escapeHtml(value);
    }

    /**
     * @param label
     * @param url
     * @return link-tag
     */
    public static String link(final String label,
            final String url)
    {
        return "<a href=" + quote(url) + ">" + label + "</a>";
    }

    /**
     * @param value
     * @return value
     */
    public static String nl2br(final String value)
    {
        return "\n" + value.replace("\r\n", "<br />")
            .replace("\r", "<br />")
            .replace("\n", "<br />");
    }

    /**
     * @param value
     * @return value
     */
    public static String space2nbsp(final String value)
    {
        return value.replace(" ", "&nbsp;");
    }

    /**
     * @param value
     * @return quote
     */
    private static String quote(final String value)
    {
        return "\"" + value + "\"";
    }
}
