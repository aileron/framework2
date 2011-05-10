package cc.aileron.dao.db;

/**
 * G2Daoサポート対象外のDB
 * 
 * @author Aileron
 */
class NotSupportDataBaseError extends Error
{
    private static final long serialVersionUID = -3903618007455579333L;

    public NotSupportDataBaseError(final String name)
    {
        super(name);
    }
}