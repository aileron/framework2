package cc.aileron.commons.properties;

import org.apache.commons.lang.StringUtils;

/**
 * composite　パターン
 * 
 * @author Aileron
 * 
 */
class CompositePropertiesItem extends CompositeProperties
{
    @Override
    public Boolean toBoolean()
    {
        return toNumber().intValue() != 0;
    }

    @Override
    public Number toNumber()
    {
        if (StringUtils.isEmpty(value))
        {
            return 0;
        }
        return Integer.valueOf(value);
    }

    @Override
    public String toString()
    {
        return value;
    }

    /**
     * 
     * @param value
     */
    public CompositePropertiesItem(final String value)
    {
        this.value = value;
    }

    /**
     * value
     */
    private final String value;
}
