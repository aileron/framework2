/**
 *
 */
package cc.aileron.container.model;

import cc.aileron.container.Provider;

/**
 * インスタンスのスコープ管理
 * 
 * @author aileron
 */
public interface Scope
{
    /**
     * バインディング情報を与えると、インスタンスを取得する
     * 
     * @param <T>
     * @param binding
     * @param creator
     * @return T
     */
    <T> Provider<T> getInstance(Binding<T> binding, Provider<T> creator);
}
