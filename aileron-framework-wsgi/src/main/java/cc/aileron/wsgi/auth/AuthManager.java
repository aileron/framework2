/**
 * 
 */
package cc.aileron.wsgi.auth;

import cc.aileron.workflow.auth.WorkflowAuth;

/**
 * 認証管理リソースのインタフェース
 * 
 * @author aileron
 * @param <T>
 *            認証情報(ユーザー情報)の型
 */
public interface AuthManager<T> extends WorkflowAuth
{
    /**
     * ログイン処理
     * 
     * @return valid ログイン処理が成功したか
     */
    boolean login();

    /**
     * ログアウト処理
     */
    void logout();

    /**
     * @return ssl
     */
    boolean ssl();

    /**
     * @return T 認証情報(ユーザー情報)
     */
    T user();
}
