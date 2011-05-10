/**
 * 
 */
package cc.aileron.php;

import com.google.inject.ImplementedBy;

/**
 * @author aileron
 */
@ImplementedBy(Pojo2PhpSourceImpl.class)
public interface Pojo2PhpSource
{
    /**
     * @param object
     * @return php source
     */
    String convert(Object object);
}
