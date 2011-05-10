/**
 * 
 */
package cc.aileron.generic.util;

/**
 * @author aileron
 * @param <Car> 
 * @param <Cdr> 
 */
public class ConsCell<Car, Cdr>
{
    /**
     * @param car 
     * @param cdr 
     * @param <Car> 
     * @param <Cdr> 
     * @return {@link ConsCell}
     * 
     */
    public static <Car, Cdr> ConsCell<Car, Cdr> cons(final Car car,
            final Cdr cdr)
    {
        return new ConsCell<Car, Cdr>(car, cdr);
    }

    /**
     * @param car
     * @param cdr
     */
    public ConsCell(final Car car, final Cdr cdr)
    {
        this.car = car;
        this.cdr = cdr;
    }

    /**
     * car
     */
    public final Car car;

    /**
     * cdr
     */
    public final Cdr cdr;

}