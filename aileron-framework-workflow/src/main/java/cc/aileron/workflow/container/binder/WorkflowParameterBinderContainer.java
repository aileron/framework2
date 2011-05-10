/**
 *
 */
package cc.aileron.workflow.container.binder;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.workflow.WorkflowParameterBinder;
import cc.aileron.workflow.parameter.WorkflowParameterBinderDefault;

import com.google.inject.Inject;

/**
 * WorkflowParameterBinder の クラスもしくはインスタンスを保持する
 * 
 * @author aileron
 */
public class WorkflowParameterBinderContainer
{
    /**
     * @return {@link WorkflowParameterBinder}
     */
    public WorkflowParameterBinder get()
    {
        if (parameterBinder != null)
        {
            return parameterBinder;
        }
        if (parameterBinderClass != null)
        {
            return instance.get(parameterBinderClass);
        }
        throw new Error("WorkflowParameterBinder not bind!!!!");
    }

    void set(final Class<? extends WorkflowParameterBinder> parameterBinderClass)
    {
        this.parameterBinderClass = parameterBinderClass;
    }

    void set(final WorkflowParameterBinder parameterBinder)
    {
        this.parameterBinder = parameterBinder;
    }

    /**
     * @param instance
     */
    @Inject
    public WorkflowParameterBinderContainer(final InstanceManager instance)
    {
        this.instance = instance;
    }

    private final InstanceManager instance;
    private WorkflowParameterBinder parameterBinder;
    private Class<? extends WorkflowParameterBinder> parameterBinderClass = WorkflowParameterBinderDefault.class;
}
