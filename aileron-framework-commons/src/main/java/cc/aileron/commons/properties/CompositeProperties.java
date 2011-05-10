package cc.aileron.commons.properties;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import cc.aileron.generic.PrimitiveWrappers.BooleanGetAccessor;
import cc.aileron.generic.PrimitiveWrappers.NumberGetAccessor;

/**
 * composite パターン
 * 
 * @author Aileron
 * 
 */
public class CompositeProperties implements
    NumberGetAccessor,
    BooleanGetAccessor
{
    /**
     * @param prop
     * @return ICompositeProperties
     */
    public static CompositeProperties load(final Properties prop)
    {
        final CompositeProperties config = new CompositePropertiesContainer();
        for (final Map.Entry<Object, Object> ent : prop.entrySet())
        {
            /*
             * プロパティのキー値をdot区切りにする
             */
            final LinkedList<String> keys = new LinkedList<String>(
                    Arrays.asList(((String) ent.getKey()).split("\\Q.\\E")));

            /*
             * 終端のキーを取得する
             */
            final String endKey = keys.pollLast();

            /*
             * 値を取得する
             */
            final CompositePropertiesItem component = new CompositePropertiesItem(
                    ent.getValue()
                        .toString());

            /*
             * キー値が一つの場合は階層構造にしない
             */
            if (keys.size() == 0)
            {
                config.set(endKey, component);
                return config;
            }

            final String key = keys.pollFirst();
            final CompositeProperties root = config.exist(key) ? config.get(key)
                    : new CompositePropertiesContainer();

            CompositeProperties c = root;
            CompositeProperties next;
            for (final String k : keys)
            {
                next = c.exist(k) ? c.get(k)
                        : new CompositePropertiesContainer();
                c.set(k, next);
                c = next;
            }

            c.set(endKey, component);
            config.set(key, root);
        }
        return config;
    }

    /**
     * @return entrySet
     */
    public Set<Entry<String, CompositeProperties>> entrySet()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * exists
     * 
     * @param key
     * @return is exist
     */
    public boolean exist(final String key)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * get
     * 
     * @param key
     * @return CompositePropertie
     */
    public CompositeProperties get(final String key)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * set
     * 
     * @param key
     * @param value
     */
    public void set(final String key,
            final CompositeProperties value)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean toBoolean()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Number toNumber()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * protected contractor
     */
    protected CompositeProperties()
    {
    }
}