/**
 * 
 */
package cc.aileron.workflow.container;

import cc.aileron.workflow.WorkflowParameter;

/**
 * @author aileron
 */
public interface WorkflowContext
{
    /**
     * パラメータ
     */
    ThreadLocal<WorkflowParameter> parameters = new ThreadLocal<WorkflowParameter>();
}
