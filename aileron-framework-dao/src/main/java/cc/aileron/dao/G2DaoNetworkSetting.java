/**
 * 
 */
package cc.aileron.dao;

import java.lang.annotation.Annotation;

import javax.sql.DataSource;

/**
 * 複数接続を設定のインタフェース
 * 
 * @author aileron
 */
public class G2DaoNetworkSetting
{
    /**
     * @return {@link Class} g2dao manager annotation
     */
    public final Class<? extends Annotation> annotation()
    {
        return annotation;
    }

    /**
     * @return {@link DataSource}
     */
    public final DataSource dataSource()
    {
        return dataSource;
    }

    /**
     * @return object キャッシュの ON/OFF フラグ
     */
    public final boolean isCacheable()
    {
        return isCacheable;
    }

    /**
     * @param annotation
     * @param dataSource
     */
    public G2DaoNetworkSetting(final Class<? extends Annotation> annotation,
            final DataSource dataSource)
    {
        this(annotation, dataSource, false);
    }

    /**
     * @param annotation
     * @param dataSource
     * @param isCacheable
     */
    public G2DaoNetworkSetting(final Class<? extends Annotation> annotation,
            final DataSource dataSource, final boolean isCacheable)
    {
        this.annotation = annotation;
        this.dataSource = dataSource;
        this.isCacheable = isCacheable;
    }

    /**
     * @param dataSource
     */
    public G2DaoNetworkSetting(final DataSource dataSource)
    {
        this(null, dataSource, false);
    }

    /**
     * @param dataSource
     * @param isCacheable
     */
    public G2DaoNetworkSetting(final DataSource dataSource,
            final boolean isCacheable)
    {
        this(null, dataSource, isCacheable);
    }

    private final Class<? extends Annotation> annotation;

    private final DataSource dataSource;
    private final boolean isCacheable;
}
