/**
 *
 */
package cc.aileron.pager;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * G2DaoPagerTest
 * 
 * @author aileron
 */
public class HtmlPagerTest
{
    /**
     * spec
     */
    @Test
    public void spec()
    {
        final HtmlPager pager = HtmlPager.factory.get(30);

        assertThat(pager.countStart(), is(0));
        assertThat(pager.countEnd(), is(0));

        pager.context().count(135);

        assertThat(pager.countAll(), is(135));
        assertThat(pager.pageCount(), is(5));
        assertThat(pager.links().size(), is(5));

        pager.pageNumber(1);
        assertThat(pager.countStart(), is(1));
        assertThat(pager.countEnd(), is(30));

        pager.pageNumber(2);
        assertThat(pager.countStart(), is(31));
        assertThat(pager.countEnd(), is(60));

        pager.pageNumber(3);
        assertThat(pager.countStart(), is(61));
        assertThat(pager.countEnd(), is(90));

        pager.pageNumber(4);
        assertThat(pager.countStart(), is(91));
        assertThat(pager.countEnd(), is(120));

        pager.pageNumber(5);
        assertThat(pager.countStart(), is(121));
        assertThat(pager.countEnd(), is(135));

        pager.pageNumber(-1);
        assertThat(pager.countStart(), is(1));
        assertThat(pager.countEnd(), is(30));

        pager.pageNumber(6);
        assertThat(pager.countStart(), is(1));
        assertThat(pager.countEnd(), is(30));
    }
}
