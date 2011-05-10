/**
 *
 */
package cc.aileron.domain;

/**
 * ドメインオブジェクト初期化用
 * 
 * @author aileron
 * @param <E>
 */
public interface DomainConfigure<E>
{
    /**
     * 対象ドメインのエンティティ
     * 
     * @return entity
     */
    E entity();

    /**
     * 指定された型のインスタンスを取得する
     * 
     * @param <I>
     * @param type
     * @return target
     */
    <I> I instance(final Class<I> type);
}
