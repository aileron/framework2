/**
 * 
 */
package cc.aileron.generic;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import cc.aileron.generic.util.Katakana;

/**
 * @author aileron
 */
public class TryKanakana
{
    /**
     * 
     */
    @Test
    public void spec()
    {
        assertThat(Katakana.fullwidth2halfwidth("１２３４５６７８９０"), is("1234567890"));
    }
}
