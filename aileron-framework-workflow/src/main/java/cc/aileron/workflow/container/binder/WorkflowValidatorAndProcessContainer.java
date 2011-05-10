/**
 *
 */
package cc.aileron.workflow.container.binder;

import static cc.aileron.generic.util.Cast.*;
import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.workflow.WorkflowJudgment;
import cc.aileron.workflow.WorkflowProcess;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class WorkflowValidatorAndProcessContainer
{
    /**
     * @author aileron
     * 
     * @param <T>
     */
    public class WorkflowValidatorAndProcess<T>
    {
        /**
         * @return process
         */
        public WorkflowProcess<T> process()
        {
            if (process != null)
            {
                return cast(process);
            }
            if (processClass != null)
            {
                return cast(instance.get(processClass));
            }
            return null;
        }

        /**
         * @return judgment
         */
        public WorkflowJudgment<T> validator()
        {
            if (validator != null)
            {
                return cast(validator);
            }
            if (validatorClass != null)
            {
                return cast(instance.get(validatorClass));
            }
            return null;
        }

        /**
         * @param validator
         * @param process
         */
        public WorkflowValidatorAndProcess(
                final Class<? extends WorkflowJudgment<? super T>> validator,
                final Class<? extends WorkflowProcess<? super T>> process)
        {
            this(null, null, validator, process);
        }

        /**
         * @param validator
         * @param process
         */
        public WorkflowValidatorAndProcess(
                final Class<? extends WorkflowJudgment<? super T>> validator,
                final WorkflowProcess<? super T> process)
        {
            this(null, process, validator, null);
        }

        /**
         * @param process
         */
        public WorkflowValidatorAndProcess(
                final Class<? extends WorkflowProcess<? super T>> process)
        {
            this(null, null, null, process);
        }

        /**
         * @param validator
         * @param process
         */
        public WorkflowValidatorAndProcess(
                final WorkflowJudgment<? super T> validator,
                final Class<? extends WorkflowProcess<? super T>> process)
        {
            this(validator, null, null, process);
        }

        /**
         * @param validator
         * @param process
         */
        public WorkflowValidatorAndProcess(
                final WorkflowJudgment<? super T> validator,
                final WorkflowProcess<? super T> process)
        {
            this(validator, process, null, null);
        }

        /**
         * @param process
         */
        public WorkflowValidatorAndProcess(
                final WorkflowProcess<? super T> process)
        {
            this(null, process, null, null);
        }

        /**
         * @param v
         * @param p
         * @param vc
         * @param pc
         */
        private WorkflowValidatorAndProcess(
                final WorkflowJudgment<? super T> v,
                final WorkflowProcess<? super T> p,
                final Class<? extends WorkflowJudgment<? super T>> vc,
                final Class<? extends WorkflowProcess<? super T>> pc)
        {
            this.process = p != null ? instance.injectMembers(p) : null;
            this.processClass = pc;
            this.validator = v != null ? instance.injectMembers(v) : null;
            this.validatorClass = vc;
        }

        /**
         * process
         */
        private final WorkflowProcess<? super T> process;

        /**
         * process-class
         */
        private final Class<? extends WorkflowProcess<? super T>> processClass;

        /**
         * validator
         */
        private final WorkflowJudgment<? super T> validator;

        /**
         * validator-class
         */
        private final Class<? extends WorkflowJudgment<? super T>> validatorClass;
    }

    /**
     * @param instance
     * @param transitionProcessFactory 
     */
    @Inject
    public WorkflowValidatorAndProcessContainer(final InstanceManager instance,
            final WorkflowTransitionProcessFactory transitionProcessFactory)
    {
        this.instance = instance;
        this.transitionProcessFactory = transitionProcessFactory;
    }

    /**
     * transitionProcessFactory
     */
    public final WorkflowTransitionProcessFactory transitionProcessFactory;

    final InstanceManager instance;
}
