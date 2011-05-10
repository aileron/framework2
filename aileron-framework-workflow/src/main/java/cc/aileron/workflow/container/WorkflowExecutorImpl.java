/**
 * Copyright (C) 2009 aileron.cc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.aileron.workflow.container;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.aileron.accessor.PojoAccessor;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowJudgment;
import cc.aileron.workflow.WorkflowParameter;
import cc.aileron.workflow.WorkflowParameterBindException;
import cc.aileron.workflow.WorkflowParameterBinder;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.WorkflowProcessError;
import cc.aileron.workflow.WorkflowProcessExecuteException;
import cc.aileron.workflow.activity.WorkflowActivityFactory;
import cc.aileron.workflow.container.binder.WorkflowValidatorAndProcessContainer.WorkflowValidatorAndProcess;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;

/**
 * @author Aileron
 * @param <Resource>
 */
public class WorkflowExecutorImpl<Resource> implements WorkflowExecutor
{
    @Override
    public final void execute(final HashMap<String, Object> rawparameters)
    {
        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        final HashMap<String, Object> urlParameters = new HashMap<String, Object>();
        final HashMap<String, Object> queryParameters = new HashMap<String, Object>();
        for (final String key : status.keys())
        {
            parameters.put(key, rawparameters.get(key));
        }
        for (final String key : status.uriParameterKeys)
        {
            urlParameters.put(key, rawparameters.get(key));
        }
        for (final String key : status.requestParameterKeys)
        {
            queryParameters.put(key, rawparameters.get(key));
        }

        WorkflowContext.parameters.set(new WorkflowParameter()
        {
            @Override
            public Object get(final String key)
            {
                return parameters.get(key);
            }

            @Override
            public Object remove(final String key)
            {
                urlParameters.remove(key);
                queryParameters.remove(key);
                return parameters.remove(key);
            }
        });
        try
        {
            ProvisionException pe = null;
            final PojoAccessor<Resource> accessor;
            {
                PojoAccessor<Resource> tmp = null;
                try
                {
                    tmp = accessorProvider.get();
                }
                catch (final ProvisionException e)
                {
                    pe = e;
                }
                accessor = tmp;
            }
            final WorkflowActivity<Resource> activity = activityFactory.create(accessor,
                    parameters);
            try
            {
                if (pe != null)
                {
                    final Throwable cause = pe.getCause();
                    if (cause instanceof Exception)
                    {
                        throw (Exception) cause;
                    }
                    throw pe;
                }
                try
                {
                    parameterBinder.bind(accessor, urlParameters);
                    parameterBinder.bind(accessor, queryParameters);
                }
                catch (final ProvisionException e)
                {
                    final Throwable cause = e.getCause();
                    if (cause instanceof Exception)
                    {
                        throw (Exception) cause;
                    }
                    throw e;
                }
                catch (final Exception e)
                {
                    throw new WorkflowParameterBindException(e, activity);
                }
                doProcess(activity);
            }
            catch (final Exception e)
            {
                Throwable c = e;
                if (e instanceof WorkflowParameterBindException
                        || e instanceof WorkflowProcessExecuteException)
                {
                    c = e.getCause();
                }
                final WorkflowProcess<Resource> errorProcess = exceptionMappings.get(c.getClass());
                if (errorProcess != null)
                {
                    try
                    {
                        logger.trace("except({})={}",
                                e.getClass(),
                                errorProcess);
                        errorProcess.doProcess(activity);
                        return;
                    }
                    catch (final Exception e1)
                    {
                        throw e1;
                    }
                }
                throw e;
            }
        }
        catch (final WorkflowProcessExecuteException e)
        {
            throw new WorkflowProcessError(e.getCause(),
                    target,
                    condition,
                    e.activity,
                    e.process);
        }
        catch (final WorkflowParameterBindException e)
        {
            throw new WorkflowProcessError(e.getCause(),
                    target,
                    condition,
                    e.activity);
        }
        catch (final Exception e)
        {
            throw new WorkflowProcessError(e.getCause(), target, condition);
        }
    }

    @Override
    public Class<?> target()
    {
        return target;
    }

    /**
     * @param activity
     * @throws Exception
     */
    private void doProcess(final WorkflowActivity<Resource> activity)
            throws WorkflowProcessExecuteException
    {
        for (final WorkflowValidatorAndProcess<Resource> vp : processList)
        {
            final WorkflowProcess<Resource> process = vp.process();
            final WorkflowJudgment<Resource> validator = vp.validator();

            try
            {

                if (validator == null)
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace("request:{} {}, resource:{}, doProcess#process : {}",
                                new Object[] { condition.method, condition.uri,
                                        activity.resource().getClass(), process });
                    }
                    process.doProcess(activity);
                    continue;
                }

                if (logger.isTraceEnabled())
                {
                    logger.trace("request:{} {}, method:{}, resource:{}, doProcess#validator : {}",
                            new Object[] { condition.method, condition.uri,
                                    activity.resource().getClass(), validator });
                }
                if (validator.doJudgment(activity))
                {
                    continue;
                }

                if (logger.isTraceEnabled())
                {
                    logger.trace("request:{} {}, resource:{}, doProcess#errorProcess : {}",
                            new Object[] { condition.method, condition.uri,
                                    activity.resource().getClass(), process });
                }
                process.doProcess(activity);
                return;

            }
            catch (final Exception e)
            {
                throw new WorkflowProcessExecuteException(e, activity, process);
            }
        }
    }

    /**
     * @param condition
     * @param target
     * @param activityFactory
     * @param accessorProvider
     * @param status
     */
    public WorkflowExecutorImpl(final WorkflowRegistryCondition condition,
            final Class<Resource> target,
            final WorkflowActivityFactory activityFactory,
            final Provider<PojoAccessor<Resource>> accessorProvider,
            final WorkflowExecutorStatus<Resource> status)
    {
        this.condition = condition;
        this.target = target;
        this.accessorProvider = accessorProvider;
        this.activityFactory = activityFactory;
        this.status = status;
        this.parameterBinder = status.parameterBinderContainer.get();
        this.exceptionMappings = status.exceptionMappings;
        this.processList = status.processList;
    }

    final WorkflowExecutorStatus<Resource> status;

    private final Provider<PojoAccessor<Resource>> accessorProvider;
    private final WorkflowActivityFactory activityFactory;
    private final WorkflowRegistryCondition condition;
    private final HashMap<Class<? extends Exception>, WorkflowProcess<Resource>> exceptionMappings;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WorkflowParameterBinder parameterBinder;
    private final List<WorkflowValidatorAndProcess<Resource>> processList;
    private final Class<Resource> target;
}