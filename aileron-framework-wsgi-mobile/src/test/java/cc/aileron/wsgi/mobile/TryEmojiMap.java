/**
 * 
 */
package cc.aileron.wsgi.mobile;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.junit.runner.guice.GuiceInjectRunner;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class TryEmojiMap
{
    /**
     */
    @Test
    public void spec()
    {
        System.out.println(emoji.get(MobileCarrier.AU, "EB7A"));

        System.out.println(emoji.get(MobileCarrier.AU, "EB2A"));
        System.out.println(emoji.get(MobileCarrier.AU, "E4AB"));

        System.out.println(emoji.get(MobileCarrier.AU, "E587"));
        System.out.println(emoji.get(MobileCarrier.AU, "EB2C"));

        System.out.println(emoji.get(MobileCarrier.AU, "E479"));
    }

    @Inject
    MobileHtmlEmojiConvertor emoji;
}
