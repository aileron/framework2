/**
 * 
 */
package cc.aileron.wsgi.auth;

import cc.aileron.workflow.WorkflowProcess;

/**
 * @author aileron
 */
public interface AuthWorkflowSetting
{
    /**
     * @return ログイン後URL
     */
    String loginedUrl();

    /**
     * @return loginUrl
     */
    String loginUrl();

    /**
     * @return ログイン画面
     * @throws Exception
     */
    WorkflowProcess<Object> loginView() throws Exception;

    /**
     * @return logoutUrl
     */
    String logoutUrl();

    /**
     * @return ログアウト画面
     * @throws Exception
     */
    WorkflowProcess<Object> logoutView() throws Exception;

    /**
     * @return ログイン時に使用するパラメータ名
     */
    String[] requestParameterKeys();

    /**
     * @return セッション管理クラス
     */
    Class<? extends AuthManager<?>> sesison();
}
