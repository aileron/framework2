/**
 * 
 */
package cc.aileron.wsgi.context;

/**
 * @author aileron
 */
public interface WsgiRequestAttribute
{
    /**
     * @param name 
     * @param key
     * @return object
     */
    Object get(String name);
}
