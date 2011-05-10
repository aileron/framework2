/**
 * 
 */
package cc.aileron.dao.db.sql;

import org.apache.commons.lang.StringEscapeUtils;

import cc.aileron.template.parser.EscapeMethod;

import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class EscapeMethodSql implements EscapeMethod
{
    @Override
    public String apply(final String source)
    {
        return StringEscapeUtils.escapeSql(source);
    }
}
