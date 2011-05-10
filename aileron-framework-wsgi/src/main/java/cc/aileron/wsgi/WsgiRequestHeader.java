/**
 * 
 */
package cc.aileron.wsgi;

import cc.aileron.generic.EString;

/**
 * @author aileron
 */
public interface WsgiRequestHeader
{
    /**
     * @param key
     * @return value
     */
    EString get(String key);
}
