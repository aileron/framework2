/**
 *
 */
package cc.aileron.container.impl;

import java.lang.annotation.Annotation;

import cc.aileron.container.Binder;
import cc.aileron.container.BinderAnnotate;
import cc.aileron.container.BinderBind;
import cc.aileron.container.BinderTo;
import cc.aileron.container.BinderToInstance;
import cc.aileron.container.Provider;
import cc.aileron.container.model.BindingMap;
import cc.aileron.container.model.Scope;
import cc.aileron.container.model.ScopeMap;

/**
 * @author aileron
 */
class BinderImpl implements Binder
{
    @Override
    public <T> BinderBind<T> bind(final Class<T> type)
    {
        return new BinderBind<T>()
        {
            @Override
            public BinderTo to(final Class<? extends T> to)
            {
                bindingMap.bindImplement(type, to, null, null);
                return new BinderTo()
                {
                    @Override
                    public BinderAnnotate annotate(final Annotation annotation)
                    {
                        bindingMap.bindImplement(type, to, annotation, null);
                        return new BinderAnnotate()
                        {
                            @Override
                            public void in(
                                    final Class<? extends Annotation> scope)
                            {
                                bindingMap.bindImplement(type,
                                        to,
                                        annotation,
                                        scope);
                            }
                        };
                    }

                    @Override
                    public void in(final Class<? extends Annotation> scope)
                    {
                        bindingMap.bindImplement(type, to, null, scope);
                    }
                };
            }

            @Override
            public BinderToInstance toInstance(final T to)
            {
                bindingMap.bindInstance(type, to, null);
                return new BinderToInstance()
                {
                    @Override
                    public void annotate(final Annotation annotation)
                    {
                        bindingMap.bindInstance(type, to, annotation);
                    }
                };
            }

            @Override
            public BinderTo toProvider(final Class<? extends Provider<T>> to)
            {
                bindingMap.bindProviderImplement(type, to, null, null);
                return new BinderTo()
                {
                    @Override
                    public BinderAnnotate annotate(final Annotation annotation)
                    {
                        bindingMap.bindProviderImplement(type, to, annotation, null);
                        return new BinderAnnotate()
                        {
                            @Override
                            public void in(
                                    final Class<? extends Annotation> scope)
                            {
                                bindingMap.bindProviderImplement(type,
                                        to,
                                        annotation,
                                        scope);
                            }
                        };
                    }

                    @Override
                    public void in(final Class<? extends Annotation> scope)
                    {
                        bindingMap.bindImplement(type, to, null, scope);
                    }
                };
            }

            @Override
            public BinderTo toProvider(final Provider<T> to)
            {
                bindingMap.bindProviderInstance(type, to, null, null);
                return new BinderTo()
                {
                    @Override
                    public BinderAnnotate annotate(final Annotation annotation)
                    {
                        bindingMap.bindProviderInstance(type,
                                to,
                                annotation,
                                null);
                        return new BinderAnnotate()
                        {
                            @Override
                            public void in(
                                    final Class<? extends Annotation> scope)
                            {
                                bindingMap.bindProviderInstance(type,
                                        to,
                                        annotation,
                                        scope);
                            }
                        };
                    }

                    @Override
                    public void in(final Class<? extends Annotation> scope)
                    {
                        bindingMap.bindProviderInstance(type, to, null, scope);
                    }
                };
            }
        };
    }

    @Override
    public void bindScope(final Class<? extends Annotation> annotation,
            final Scope scope)
    {
        scopeMap.put(annotation, scope);
    }

    /**
     * @param bindingMap
     * @param scopeMap
     */
    public BinderImpl(final BindingMap bindingMap, final ScopeMap scopeMap)
    {
        this.bindingMap = bindingMap;
        this.scopeMap = scopeMap;
    }

    private final BindingMap bindingMap;
    private final ScopeMap scopeMap;
}
