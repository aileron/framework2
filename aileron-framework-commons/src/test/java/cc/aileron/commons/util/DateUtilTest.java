/**
 * 
 */
package cc.aileron.commons.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

/**
 * @author aileron
 */
public class DateUtilTest
{
    /**
     */
    @Test
    public void spec()
    {
        assertThat(DateUtil.diffYear(Date.valueOf("2011-01-01"),
                Date.valueOf("1960-01-01")),
                is(51));

        assertThat(DateUtil.diffYear(Date.valueOf("2011-01-01"),
                Date.valueOf("2020-01-01")),
                is(-9));

        assertThat(DateUtil.diffYear(Date.valueOf("2011-01-01"),
                Date.valueOf("1980-01-01")),
                is(31));

        assertThat(DateUtil.diffYear(Date.valueOf("2039-01-01"),
                Date.valueOf("1960-01-01")),
                is(79));

    }
}
