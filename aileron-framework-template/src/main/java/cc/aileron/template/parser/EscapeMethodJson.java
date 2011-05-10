/**
 * 
 */
package cc.aileron.template.parser;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class EscapeMethodJson implements EscapeMethod
{
    @Override
    public String apply(final String source)
    {
        return StringEscapeUtils.escapeJavaScript(source);
    }
}
