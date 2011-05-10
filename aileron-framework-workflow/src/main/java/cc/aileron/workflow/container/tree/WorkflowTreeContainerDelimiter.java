/**
 * 
 */
package cc.aileron.workflow.container.tree;

import com.google.inject.ImplementedBy;

/**
 * @author aileron
 */
@ImplementedBy(WorkflowTreeContainerDelimiterDefault.class)
public interface WorkflowTreeContainerDelimiter
{
    /**
     * @return delimiter
     */
    String value();
}
