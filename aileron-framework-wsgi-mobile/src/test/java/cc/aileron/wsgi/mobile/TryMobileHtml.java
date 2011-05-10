/**
 *
 */
package cc.aileron.wsgi.mobile;

import java.util.EnumMap;

import cc.aileron.commons.util.ResourceUtils;
import cc.aileron.template.Template;
import cc.aileron.template.TemplateModules;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * css-loader
 * 
 * @author aileron
 */
public class TryMobileHtml
{
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(final String[] args) throws Exception
    {
        final Injector in = Guice.createInjector(new TemplateModules());
        final MobileHtmlTemplateManager htmlManager = in.getInstance(MobileHtmlTemplateManager.class);
        final String html = ResourceUtils.vfsResource("http://aileron.cc")
                .toString();
        final CssSelectorProperties css = in.getInstance(CssSelectorPropertiesFactory.class)
                .parse(ResourceUtils.vfsResource("http://aileron.cc/css/screen.css")
                        .toString());
        final EnumMap<MobileCarrier, Template> htmls = htmlManager.get(html,
                css);
        System.out.println(htmls.get(MobileCarrier.DOCOMO));
    }
}
