/**
 * 
 */
package cc.aileron.commons.util;

import java.util.HashMap;

/**
 * @author aileron
 */
public class CommandLineOptions
{
    /**
     * @param key
     * @return longoption value
     */
    public String get(final String key)
    {
        return args.get(key);
    }

    /**
     * @param key
     * @return value
     */
    public boolean getBool(final String key)
    {
        return args.containsKey(key) ? Boolean.parseBoolean(args.get(key))
                : false;
    }

    /**
     * @param key
     * @return value
     */
    public int getInt(final String key)
    {
        return args.containsKey(key) ? Integer.parseInt(args.get(key)) : 0;
    }

    /**
     * @param args
     */
    public CommandLineOptions(final String[] args)
    {
        for (final String arg : args)
        {
            final String[] token = arg.split("=");
            String key = token[0];
            final String value = token[1];
            key = key.indexOf('-') == 0 ? key.substring(1) : key;
            this.args.put(key, value);
        }
    }

    final HashMap<String, String> args = new HashMap<String, String>();
}
