/**
 * 
 */
package cc.aileron.commons.util;

import java.sql.Date;

/**
 * @author aileron
 */
public abstract class DateUtil
{
    /**
     * 一年間の秒数 (ミリ秒)
     */
    static final long YEAR_MILL_SEC = 31536000000l;

    /**
     * @param cmp
     * @return 現在時刻との差の年数を返す
     */
    public static int diffYear(final Date cmp)
    {
        final long current = System.currentTimeMillis();
        return diffYear(new Date(current), cmp);
    }

    /**
     * @param dist
     *            1970年より後じゃないとヤバイ!!!
     * @param cmp
     * @return 対象との差の年数を返す
     */
    public static int diffYear(final Date dist, final Date cmp)
    {
        final long current = dist.getTime();
        final long cmptime = cmp.getTime();
        if (cmptime > 0)
        {
            return (int) ((current - cmptime) / YEAR_MILL_SEC);
        }
        return (int) ((current + (cmptime * -1)) / YEAR_MILL_SEC);
    }
}
