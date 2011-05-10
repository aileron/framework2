/**
 * 
 */
package cc.aileron.domain;

/**
 * @author aileron
 * @param <Domain> 
 */
public interface DomainFactory<Domain>
{
    /**
     * @param <Entity>
     * @param entity
     * @return Domain
     * @throws DomainInitializedError
     */
    <Entity> Domain get(Entity entity) throws DomainInitializedError;
}
