package cc.aileron.workflow;

import cc.aileron.generic.util.InterconversionMap;

/**
 * Cat
 * 
 * @author aileron
 * 
 */
public enum WorkflowParameterBinderSampleCat
{
    /**
     * A
     */
    A(1),

    /**
     * B
     */
    B(2);

    /**
     * @param value
     * @return Cat
     */
    public static WorkflowParameterBinderSampleCat valueOf(final int value)
    {
        return InterconversionMap.category(WorkflowParameterBinderSampleCat.class)
                .convert(value);
    }

    private WorkflowParameterBinderSampleCat(final int intValue)
    {
        InterconversionMap.category(WorkflowParameterBinderSampleCat.class)
                .set(intValue, this);
    }
}