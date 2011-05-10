package cc.aileron.wsgi.mobile;

import static cc.aileron.commons.util.ResourceUtils.*;

import java.util.Map.Entry;

import com.google.inject.Guice;

/**
 * @author aileron
 */
public class TryCssConvert
{
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(final String... args) throws Exception
    {
        final CssSelectorPropertiesFactory css2xpath = Guice.createInjector()
                .getInstance(CssSelectorPropertiesFactory.class);

        final CssSelectorProperties css = css2xpath.parse(resource("common.css").toString());
        for (final Entry<String, String> e : css)
        {
            System.out.println(e.getKey() + "=>" + e.getValue());
        }
    }
}
