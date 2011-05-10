/**
 *
 */
package cc.aileron.wsgi.mobile;

import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.commons.util.ResourceUtils;

import com.google.inject.Guice;

/**
 * @author aileron
 * 
 */
public class TryMobileEmoji
{
    /**
     * @param args
     * @throws ResourceNotFoundException
     */
    public static void main(final String[] args)
            throws ResourceNotFoundException
    {
        final String html = ResourceUtils.resource("sample.html").toString();

        final MobileHtmlEmojiConvertor conv = Guice.createInjector()
                .getInstance(MobileHtmlEmojiConvertor.class);

        final String convertHtml = conv.convert(MobileCarrier.DOCOMO,
                MobileCarrier.AU,
                html);

        System.out.println(convertHtml);
    }
}
