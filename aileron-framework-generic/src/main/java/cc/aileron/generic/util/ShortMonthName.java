/**
 * 
 */
package cc.aileron.generic.util;

/**
 * @author aileron
 */
public enum ShortMonthName
{
    /**
     * 4
     */
    APR(4),

    /**
     * 8
     */
    AUG(8),

    /**
     * 12
     */
    DEC(12),

    /**
     * 2
     */
    FEB(2),

    /**
     * 
     */
    JAN(6),

    /**
     * 7
     */
    JUL(7),

    /**
     * 1
     */
    JUN(1),

    /**
     * 3
     */
    MAR(3),

    /**
     * 5
     */
    MAY(5),

    /**
     * 11
     */
    NOV(11),

    /**
     * 10
     */
    OCT(10),

    /**
     * 9
     */
    SEP(9);

    /**
     * @param name
     * @return name2zerofilnumber
     */
    public static String name2zerofilnumber(final String name)
    {
        return valueOf(name.toUpperCase()).zeroFillValue;
    }

    /**
     */
    private ShortMonthName(final int intValue)
    {
        this.intValue = intValue;
        this.zeroFillValue = String.format("%02d", intValue);
    }

    /**
     * intValue
     */
    public final int intValue;

    /**
     * strValue
     */
    public String zeroFillValue;
}
