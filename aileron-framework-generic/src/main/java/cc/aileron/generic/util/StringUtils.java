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

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Aileron
 */
public class StringUtils
{
    /**
     * 「%16進数」形式にエスケープする。
     * 
     * @param string
     * @return string
     */
    public static String escape(final String string)
    {
        return escape(string, Charset.forName("UTF-8"));
    }

    /**
     * 「%16進数」形式にエスケープする。
     * 
     * @param string
     *            エスケープする文字列
     * @param encode
     *            文字列のエンコード
     * @return エスケープした文字列
     */
    public static final String escape(final String string, final Charset encode)
    {
        final StringBuilder buff = new StringBuilder();
        final byte[] b = string.getBytes(encode);
        for (int i = 0; i < b.length; i++)
        {
            buff.append("%").append(String.format("%1$02X", b[i]));
        }
        return buff.toString();
    }

    /**
     * @param stream
     * @param length
     * @return string
     * @throws IOException
     */
    public static String input2string(final InputStream stream,
            final int length) throws IOException
    {
        final byte[] buff = new byte[length];
        stream.read(buff);
        return new String(buff);
    }

    /**
     * @param with
     * @param array
     * @return join string
     */
    public static String join(final String with, final List<String> array)
    {
        final StringBuffer buf = new StringBuffer();
        for (final String s : array)
        {
            if (buf.length() > 0)
            {
                buf.append(with);
            }
            buf.append(s);
        }
        return buf.toString();
    }

    /**
     * @param with
     * @param array
     * @return join string
     */
    public static String join(final String with, final String... array)
    {
        final StringBuffer buf = new StringBuffer();
        for (final String s : array)
        {
            if (buf.length() > 0)
            {
                buf.append(with);
            }
            buf.append(s);
        }
        return buf.toString();
    }

    /**
     * 「%16進数」形式をアンエスケープする。
     * 
     * @param string
     * @return string
     */
    public static String unescape(final String string)
    {
        return unescape(string, Charset.forName("UTF-8"));
    }

    /**
     * Decodes a <code>application/x-www-form-urlencoded</code> string using a
     * specific encoding scheme. The supplied encoding is used to determine what
     * characters are represented by any consecutive sequences of the form "
     * <code>%<i>xy</i></code>".
     * <p>
     * <em><strong>Note:</strong> The <a href=
     * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that
     * UTF-8 should be used. Not doing so may introduce
     * incompatibilites.</em>
     * 
     * @param s
     *            the <code>String</code> to decode
     * @param enc
     *            The name of a supported <a
     *            href="../lang/package-summary.html#charenc">character
     *            encoding</a>.
     * @return the newly decoded <code>String</code>
     * @see URLEncoder#encode(java.lang.String, java.lang.String)
     * @since 1.4
     */
    public static String unescape(final String s, final Charset enc)
    {

        boolean needToChange = false;
        final int numChars = s.length();
        final StringBuffer sb = new StringBuffer(numChars > 500 ? numChars / 2
                : numChars);
        int i = 0;

        char c;
        byte[] bytes = null;
        while (i < numChars)
        {
            c = s.charAt(i);
            switch (c)
            {
            case '+':
                sb.append(' ');
                i++;
                needToChange = true;
                break;
            case '%':
                /*
                 * Starting with this instance of %, process all consecutive
                 * substrings of the form %xy. Each substring %xy will yield a
                 * byte. Convert all consecutive bytes obtained this way to
                 * whatever character(s) they represent in the provided
                 * encoding.
                 */

                try
                {

                    // (numChars-i)/3 is an upper bound for the number
                    // of remaining bytes
                    if (bytes == null)
                    {
                        bytes = new byte[(numChars - i) / 3];
                    }
                    int pos = 0;

                    while (((i + 2) < numChars) && (c == '%'))
                    {
                        bytes[pos++] = (byte) Integer.parseInt(s.substring(i + 1,
                                i + 3),
                                16);
                        i += 3;
                        if (i < numChars)
                        {
                            c = s.charAt(i);
                        }
                    }

                    // A trailing, incomplete byte encoding such as
                    // "%x" will cause an exception to be thrown

                    if ((i < numChars) && (c == '%'))
                    {
                        throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
                    }

                    sb.append(new String(bytes, 0, pos, enc));
                }
                catch (final NumberFormatException e)
                {
                    throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - "
                            + e.getMessage());
                }
                needToChange = true;
                break;
            default:
                sb.append(c);
                i++;
                break;
            }
        }

        return (needToChange ? sb.toString() : s);
    }

    /**
     * private constractor
     */
    private StringUtils()
    {
    }
}
