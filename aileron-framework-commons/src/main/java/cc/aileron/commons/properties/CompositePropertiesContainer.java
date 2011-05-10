package cc.aileron.commons.properties;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * composite container
 * 
 * @author Aileron
 * 
 */
class CompositePropertiesContainer extends CompositeProperties
{
    /*
     * (非 Javadoc)
     * 
     * @see cc.aileron.commons.properties.Composite#entrySet()
     */
    @Override
    public Set<Entry<String, CompositeProperties>> entrySet()
    {
        return this.hash.entrySet();
    }

    /*
     * (非 Javadoc)
     * 
     * @see cc.aileron.commons.properties.Composite#exists()
     */
    @Override
    public boolean exist(final String key)
    {
        return this.hash.containsKey(key);
    }

    /*
     * (非 Javadoc)
     * 
     * @see cc.aileron.commons.properties.Composite#get(java.lang.String)
     */
    @Override
    public CompositeProperties get(final String key)
    {
        return this.hash.get(key);
    }

    /*
     * (非 Javadoc)
     * 
     * @see cc.aileron.commons.properties.Composite#set(java.lang.String,
     * cc.aileron.commons.properties.Composite)
     */
    @Override
    public void set(final String key,
            final CompositeProperties value)
    {
        this.hash.put(key, value);
    }

    /**
     * container hash
     */
    private final HashMap<String, CompositeProperties> hash = new HashMap<String, CompositeProperties>();
}
