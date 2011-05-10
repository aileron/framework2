/**
 * 
 */
package cc.aileron.wsgi.auth;

import static cc.aileron.workflow.WorkflowMethod.*;
import static cc.aileron.wsgi.context.WsgiContextProvider.*;
import cc.aileron.generic.util.Cast;
import cc.aileron.workflow.WorkflowActivity;
import cc.aileron.workflow.WorkflowBinder;
import cc.aileron.workflow.WorkflowConfigure;
import cc.aileron.workflow.WorkflowJudgment;
import cc.aileron.workflow.WorkflowProcess;
import cc.aileron.workflow.WorkflowTransition;
import cc.aileron.workflow.auth.WorkflowAuthExecutor;
import cc.aileron.workflow.container.binder.WorkflowTransitionProcessFactory;

import com.google.inject.Inject;

/**
 * 認証処理用のConfigure
 * 
 * 適宜 new して install して下さい
 * 
 * @author aileron
 */
public class AuthWorkflowConfigure implements WorkflowConfigure
{
    @Override
    public void configure(final WorkflowBinder binder) throws Exception
    {
        /**
         * VIEW : ログイン
         */
        binder.bind(setting.sesison())
                .method(GET)
                .uri(setting.loginUrl())
                .to()
                .process(WorkflowAuthExecutor.class, setting.loginView())
                .process(WorkflowTransition.LOCALREDIRECT, setting.loginedUrl());

        /**
         * EXEC : ログイン
         */
        binder.bind(setting.sesison())
                .method(POST)
                .uri(setting.loginUrl())
                .to()
                .requestParameterKeys(setting.requestParameterKeys())
                .process(new WorkflowJudgment<AuthManager<?>>()
                {
                    @Override
                    public boolean doJudgment(
                            final WorkflowActivity<AuthManager<?>> activity)
                            throws Exception
                    {
                        return activity.resource().login();
                    }
                },
                        setting.loginView())
                .process(new WorkflowProcess<AuthManager<?>>()
                {
                    @Override
                    public void doProcess(
                            final WorkflowActivity<AuthManager<?>> activity)
                            throws Exception
                    {
                        final String path = context().request()
                                .getServletPath();
                        final String bpath = context().request()
                                .getHeader("Referer");
                        final boolean returnToIsEmpty = bpath == null
                                || bpath.equals(path);
                        final String returnTo = returnToIsEmpty ? setting.loginedUrl()
                                : bpath;

                        final WorkflowProcess<Object> redirect = transitionProcessFactory.create(WorkflowTransition.REDIRECT,
                                activity.resource().ssl() ? returnTo.replace("http://",
                                        "https://")
                                        : returnTo);
                        redirect.doProcess(Cast.<WorkflowActivity<Object>> cast(activity));
                    }

                    @Inject
                    private final WorkflowTransitionProcessFactory transitionProcessFactory = null;

                });

        /**
         * VIEW : ログアウト
         */
        binder.bind(setting.sesison())
                .method(GET)
                .uri(setting.logoutUrl())
                .to()
                .process(WorkflowAuthExecutor.class,
                        WorkflowTransition.LOCALREDIRECT,
                        setting.loginUrl())
                .process(new WorkflowProcess<AuthManager<?>>()
                {
                    @Override
                    public void doProcess(
                            final WorkflowActivity<AuthManager<?>> activity)
                            throws Exception
                    {
                        activity.resource().logout();
                    }
                })
                .process(setting.logoutView());

    }

    /**
     * @param setting
     */
    public AuthWorkflowConfigure(final AuthWorkflowSetting setting)
    {
        this.setting = setting;
    }

    final AuthWorkflowSetting setting;
}
