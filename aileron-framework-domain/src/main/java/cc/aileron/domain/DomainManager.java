/**
 *
 */
package cc.aileron.domain;

import com.google.inject.ImplementedBy;

/**
 * ドメインクラスのインスタンスへとコンバートする為のインスタンスを管理する
 * 
 * @author aileron
 */
@ImplementedBy(DomainManagerImpl.class)
public interface DomainManager
{
    /**
     * @param <Domain>
     * @param domainClass
     * @return {@link DomainFactory}
     */
    <Domain> DomainFactory<Domain> from(Class<Domain> domainClass);

    /**
     * @param <Domain>
     * @param <Entity>
     * @param domainClass
     * @param entity
     * @return domain
     * @throws DomainInitializedError
     */
    <Domain, Entity> Domain get(Class<Domain> domainClass, Entity entity)
            throws DomainInitializedError;
}
