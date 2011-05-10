/**
 * 
 */
package cc.aileron.workflow;

/**
 * @author aileron
 */
public interface WorkflowParameter
{
    /**
     * @param key
     * @return object
     */
    Object get(String key);

    /**
     * @param key
     * @return object
     */
    Object remove(String key);
}
