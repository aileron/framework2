/**
 *
 */
package cc.aileron.domain;

/**
 * ドメイン初期化時のエラー
 * 
 * @author aileron
 */
public class DomainInitializedError extends Error
{
    enum Cause
    {
        ANNOTATION_NOT_FOUND("annotation not found"), CONSTRACTOR_NOT_FOUND(
                "constractor not found");

        private Cause(final String message)
        {
            this.message = message;
        }

        final String message;
    }

    private static final long serialVersionUID = -4335798907859319667L;

    /**
     * @param target
     * @return {@link DomainInitializedError}
     */
    public static DomainInitializedError notFoundAnnotation(
            final Class<?> target)
    {
        return new DomainInitializedError("annotation not found "
                + target.getName());
    }

    /**
     * @param implementClass
     * @param entityClass
     * @return {@link DomainInitializedError}
     */
    public static DomainInitializedError notFoundConstractor(
            final Class<?> implementClass, final Class<?> entityClass)
    {
        return new DomainInitializedError("not found constractor "
                + implementClass.getName() + "(" + entityClass + ")");
    }

    /**
     * @param domain
     * @return {@link DomainInitializedError}
     */
    public static DomainInitializedError notFoundEntity(final Class<?> domain)
    {
        return new DomainInitializedError("entity not found "
                + domain.getClass().getName());
    }

    /**
     * @param th
     */
    public DomainInitializedError(final Throwable th)
    {
        super(th);
    }

    /**
     * @param message
     */
    private DomainInitializedError(final String message)
    {
        super(message);
    }
}