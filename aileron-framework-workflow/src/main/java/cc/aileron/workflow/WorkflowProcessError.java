/**
 * 
 */
package cc.aileron.workflow;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import cc.aileron.workflow.container.WorkflowRegistryCondition;

/**
 * @author aileron
 */
public class WorkflowProcessError extends Error
{
    private static final long serialVersionUID = 1L;

    /**
     * @param exception
     * @param type
     * @param condition
     */
    public WorkflowProcessError(final Throwable exception, final Class<?> type,
            final WorkflowRegistryCondition condition)
    {
        super(String.format("error (class=%s, method=%s, url=%s, process=object-init)",
                type,
                condition.method,
                condition.uri),
                exception);
    }

    /**
     * @param exception
     * @param type
     * @param condition
     * @param activity
     */
    public WorkflowProcessError(final Throwable exception, final Class<?> type,
            final WorkflowRegistryCondition condition,
            final WorkflowActivity<?> activity)
    {
        super(String.format("error (class=%s, method=%s, url=%s, process=parameter-binding) = %s",
                type,
                condition.method,
                condition.uri,
                ReflectionToStringBuilder.toString(activity.resource())),
                exception);
    }

    /**
     * @param exception
     * @param type
     * @param condition
     * @param activity
     * @param process
     */
    public WorkflowProcessError(final Throwable exception, final Class<?> type,
            final WorkflowRegistryCondition condition,
            final WorkflowActivity<?> activity, final WorkflowProcess<?> process)
    {
        super(String.format("error (class=%s, method=%s, url=%s, process=%s) = %s",
                type,
                condition.method,
                condition.uri,
                process,
                ReflectionToStringBuilder.toString(activity.resource())),
                exception);
    }
}
