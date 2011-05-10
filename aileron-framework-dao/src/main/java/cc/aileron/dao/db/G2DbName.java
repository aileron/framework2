package cc.aileron.dao.db;

import cc.aileron.dao.db.exec.G2DaoSerialGeneratedKeys;
import cc.aileron.dao.db.exec.G2DaoSerialNoneGeneratedKeys;

/**
 * DB名の列挙型
 * 
 * @author Aileron
 */
public enum G2DbName
{
    /**
     * h2
     */
    H2,

    /**
     * mysql
     */
    MYSQL,

    /**
     * postgresql
     */
    POSTGRESQL,

    /**
     * unknown
     */
    UNKNOWN;

    /**
     * @param value
     * @return DbName
     */
    public static G2DbName convert(final String value)
    {
        try
        {
            return valueOf(value.toUpperCase());
        }
        catch (final Exception e)
        {
            throw new NotSupportDataBaseError(value);
        }
    }

    /**
     * @return シリアル関連のクラス
     */
    public Class<? extends G2DaoSerial> getSerial()
    {
        switch (this)
        {
        case MYSQL:
            return G2DaoSerialGeneratedKeys.class;
        case H2:
            return G2DaoSerialGeneratedKeys.class;

        default:
            return G2DaoSerialNoneGeneratedKeys.class;
        }
    }
}