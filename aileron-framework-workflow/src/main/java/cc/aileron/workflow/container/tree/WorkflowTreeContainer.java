/**
 *
 */
package cc.aileron.workflow.container.tree;

import java.util.Map;
import java.util.Set;

import cc.aileron.workflow.WorkflowMethod;

import com.google.inject.ImplementedBy;

/**
 * @author aileron
 */
@ImplementedBy(WorkflowTreeContainerImpl.class)
public interface WorkflowTreeContainer
{
    /**
     * バインドされているurlツリー
     * 
     * @return tree
     */
    Map<String, Integer> all();

    /**
     * @param uri
     * @param ext
     * @param method
     * @param uriparameter
     * @param requestParameter
     * @param names
     * @param key
     * @return id
     */
    int get(String uri, WorkflowMethod method,
            Map<String, Object> uriparameter, Set<String> requestParameter);

    /**
     * @param uri
     * @param ext
     * @param method
     * @param overrideKey
     * @param key
     * @param id
     */
    void put(String uri, WorkflowMethod method, int id);

    /**
     * @param uri
     * @param ext
     * @param method
     * @param overrideKey
     * @param key
     * @param id
     */
    void put(String uri, WorkflowMethod method, String overrideKey, int id);
}
