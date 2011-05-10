/**
 * 
 */
package cc.aileron.workflow.container.tree;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class WorkflowTreeContainerDelimiterDefault implements
        WorkflowTreeContainerDelimiter
{
    @Override
    public String value()
    {
        return delimiter;
    }

    /**
     */
    @Inject
    public WorkflowTreeContainerDelimiterDefault()
    {
        this.delimiter = "[_/]";
    }

    /**
     * @param delimiter
     */
    public WorkflowTreeContainerDelimiterDefault(final String delimiter)
    {
        this.delimiter = delimiter;
    }

    private final String delimiter;
}
