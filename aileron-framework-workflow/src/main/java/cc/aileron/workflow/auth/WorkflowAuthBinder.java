/**
 * 
 */
package cc.aileron.workflow.auth;

import java.util.Collection;

import cc.aileron.commons.instance.InstanceManager;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowBinder;
import cc.aileron.workflow.WorkflowConfigure;
import cc.aileron.workflow.WorkflowJudgment;
import cc.aileron.workflow.WorkflowMethod;
import cc.aileron.workflow.WorkflowParameterBinder;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.WorkflowTransition;
import cc.aileron.workflow.container.binder.WorkflowBinderBind;
import cc.aileron.workflow.container.binder.WorkflowBinderTo;

import com.google.inject.Inject;

/**
 * @author aileron
 */
public class WorkflowAuthBinder implements WorkflowBinder
{
    @Override
    public <T> WorkflowBinderBind<T> bind(final Class<T> target)
    {
        final WorkflowBinderBind<T> bind = binder.bind(target);
        return new WorkflowBinderBind<T>()
        {
            @Override
            public WorkflowBinderBind<T> isThrough(final boolean isThrough)
            {
                bind.isThrough(isThrough);
                return this;
            }

            @Override
            public WorkflowBinderBind<T> method(final WorkflowMethod method)
            {
                bind.method(method);
                return this;
            }

            @Override
            public WorkflowBinderBind<T> overrideKey(final String overrideKey)
            {
                bind.overrideKey(overrideKey);
                return this;
            }

            @Override
            public WorkflowBinderTo<T> to()
            {
                final WorkflowBinderTo<T> to = bind.to();
                to.process(judgment, WorkflowTransition.FORWARD, location);
                return new WorkflowBinderTo<T>()
                {

                    @Override
                    public WorkflowBinderTo<T> exception(
                            final Class<? extends Exception> exception,
                            final WorkflowProcess<? super T> process)
                    {
                        to.exception(exception, process);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> exception(
                            final Class<? extends Exception> exception,
                            final WorkflowTransition transition,
                            final String... value)
                    {
                        to.exception(exception, transition, value);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> parameterBinder(
                            final Class<? extends WorkflowParameterBinder> parameterBinder)
                    {
                        to.parameterBinder(parameterBinder);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> parameterBinder(
                            final WorkflowParameterBinder parameterBinder)
                    {
                        to.parameterBinder(parameterBinder);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final Class<? extends WorkflowJudgment<? super T>> validator,
                            final Class<? extends WorkflowProcess<? super T>> process)
                    {
                        to.process(validator, process);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final Class<? extends WorkflowJudgment<? super T>> validator,
                            final WorkflowProcess<? super T> process)
                    {
                        to.process(validator, process);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final Class<? extends WorkflowJudgment<? super T>> validator,
                            final WorkflowTransition transition,
                            final String... value)
                    {
                        to.process(validator, transition, value);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final Class<? extends WorkflowProcess<? super T>> process)
                    {
                        to.process(process);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final WorkflowJudgment<? super T> validator,
                            final Class<? extends WorkflowProcess<? super T>> process)
                    {
                        to.process(validator, process);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final WorkflowJudgment<? super T> validator,
                            final WorkflowProcess<? super T> process)
                    {
                        to.process(validator, process);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final WorkflowJudgment<? super T> validator,
                            final WorkflowTransition transition,
                            final String... value)
                    {
                        to.process(validator, transition, value);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final WorkflowProcess<? super T> process)
                    {
                        to.process(process);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> process(
                            final WorkflowTransition transition,
                            final String... value)
                    {
                        to.process(transition, value);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> requestParameterKeys(
                            final Class<?> requestParameterInterface)
                    {
                        to.requestParameterKeys(requestParameterInterface);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> requestParameterKeys(
                            final Class<?> requestParameterInterface,
                            final String parentKey)
                    {
                        to.requestParameterKeys(requestParameterInterface,
                                parentKey);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> requestParameterKeys(
                            final Collection<String> keys)
                    {
                        to.requestParameterKeys(keys);
                        return this;
                    }

                    @Override
                    public WorkflowBinderTo<T> requestParameterKeys(
                            final String... keys)
                    {
                        to.requestParameterKeys(keys);
                        return this;
                    }
                };
            }

            @Override
            public WorkflowBinderBind<T> uri(final String uri)
            {
                bind.uri(uri);
                return this;
            }
        };
    }

    @Override
    public void install(final WorkflowConfigure configure) throws Exception
    {
        binder.install(configure);
    }

    /**
     * @param binder
     * @param location
     * @param oauth
     */
    public WorkflowAuthBinder(final WorkflowBinder binder,
            final String location, final Class<WorkflowAuth> oauth)
    {
        this.binder = binder;
        this.location = location;
        this.judgment = new WorkflowJudgment<Object>()
        {
            @Override
            public boolean doJudgment(final WorkflowActivity<Object> activity)
                    throws Exception
            {
                if (auth.isAuth())
                {
                    return true;
                }
                return false;
            }

            /**
             * @param instance
             */
            @SuppressWarnings("unused")
            @Inject
            void inject(final InstanceManager instance)
            {
                this.auth = instance.get(oauth);
            }

            private WorkflowAuth auth;
        };
    }

    /**
     * @param binder
     * @param location
     * @param oauth
     */
    public WorkflowAuthBinder(final WorkflowBinder binder,
            final String location, final WorkflowAuth oauth)
    {
        this.binder = binder;
        this.location = location;
        this.judgment = new WorkflowJudgment<Object>()
        {
            @Override
            public boolean doJudgment(final WorkflowActivity<Object> activity)
                    throws Exception
            {
                return auth.isAuth();
            }

            /**
             * @param instance
             */
            @SuppressWarnings("unused")
            @Inject
            void inject(final InstanceManager instance)
            {
                this.auth = instance.injectMembers(oauth);
            }

            private WorkflowAuth auth;

        };
    }

    final WorkflowBinder binder;
    final WorkflowJudgment<Object> judgment;
    final String location;
}
