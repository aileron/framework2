/**
 * 
 */
package cc.aileron.dao.e;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author aileron
 */
public class SampleA
{
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     */
    public SampleA()
    {
    }

    /**
     * @param cnt
     * @param sum
     */
    public SampleA(final int cnt, final int sum)
    {
        this.cnt = cnt;
        this.sum = sum;
    }

    /**
     * cnt
     */
    public int cnt;

    /**
     * sum
     */
    public int sum;
}