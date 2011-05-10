/**
 *
 */
package cc.aileron.workflow;

import java.util.Map;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.workflow.parameter.WorkflowParameterBinderDefault;

import com.google.inject.ImplementedBy;

/**
 * リクエストパラメータを、リソースオブジェクトにマッピングする為の インタフェース
 * 
 * @author aileron
 */
@ImplementedBy(WorkflowParameterBinderDefault.class)
public interface WorkflowParameterBinder
{
    /**
     * @param <T>
     * @param accessor
     * @param p
     * @param keys
     * @throws Exception
     */
    <T> void bind(PojoAccessor<T> accessor, Map<String, Object> p)
            throws Exception;
}
