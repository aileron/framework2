/**
 *
 */
package cc.aileron.commons.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.aileron.generic.util.XRange;

/**
 * @author aileron
 */
public class XRangeTest
{
    /**
     * from-to
     */
    @Test
    public void fromTo()
    {
        final List<Integer> list = XRange.fromTo(5, 10).toList();
        assertThat(list.get(0), is(5));
        assertThat(list.get(1), is(6));
        assertThat(list.get(2), is(7));
        assertThat(list.get(3), is(8));
        assertThat(list.get(4), is(9));
        assertThat(list.get(5), is(10));

        final List<Integer> ilist = new ArrayList<Integer>();
        for (final Integer i : XRange.fromTo(5, 10))
        {
            ilist.add(i);
        }
        assertThat(ilist.get(0), is(5));
        assertThat(ilist.get(1), is(6));
        assertThat(ilist.get(2), is(7));
        assertThat(ilist.get(3), is(8));
        assertThat(ilist.get(4), is(9));
        assertThat(ilist.get(5), is(10));
    }

    /**
     * spec
     */
    @Test
    public void range1()
    {
        final List<Integer> list = XRange.xrange(5).toList();
        assertThat(list.get(0), is(0));
        assertThat(list.get(1), is(1));
        assertThat(list.get(2), is(2));
        assertThat(list.get(3), is(3));
        assertThat(list.get(4), is(4));

        final List<Integer> ilist = new ArrayList<Integer>();
        for (final Integer i : XRange.xrange(5))
        {
            ilist.add(i);
        }
        assertThat(ilist.get(0), is(0));
        assertThat(ilist.get(1), is(1));
        assertThat(ilist.get(2), is(2));
        assertThat(ilist.get(3), is(3));
        assertThat(ilist.get(4), is(4));
    }

    /**
     * spec
     */
    @Test
    public void range2()
    {
        final List<Integer> list = XRange.xrange(5, 10).toList();
        assertThat(list.get(0), is(5));
        assertThat(list.get(1), is(6));
        assertThat(list.get(2), is(7));
        assertThat(list.get(3), is(8));
        assertThat(list.get(4), is(9));

        final List<Integer> ilist = new ArrayList<Integer>();
        for (final Integer i : XRange.xrange(5, 10))
        {
            ilist.add(i);
        }
        assertThat(ilist.get(0), is(5));
        assertThat(ilist.get(1), is(6));
        assertThat(ilist.get(2), is(7));
        assertThat(ilist.get(3), is(8));
        assertThat(ilist.get(4), is(9));
    }

    /**
     * spec
     */
    @Test
    public void range3()
    {
        final List<Integer> list = XRange.xrange(2, 10, 2).toList();
        assertThat(list.get(0), is(2));
        assertThat(list.get(1), is(4));
        assertThat(list.get(2), is(6));
        assertThat(list.get(3), is(8));

        final List<Integer> ilist = new ArrayList<Integer>();
        for (final Integer i : XRange.xrange(2, 10, 2))
        {
            ilist.add(i);
        }
        assertThat(ilist.get(0), is(2));
        assertThat(ilist.get(1), is(4));
        assertThat(ilist.get(2), is(6));
        assertThat(ilist.get(3), is(8));
    }

}
