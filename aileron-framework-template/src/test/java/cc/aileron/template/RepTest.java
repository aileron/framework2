/**
 * 
 */
package cc.aileron.template;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.aileron.junit.runner.guice.GuiceInjectRunner;
import cc.aileron.template.parser.method.RepMethodParserProvider;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class RepTest
{
    /**
     * 
     */
    @Test
    public void spec()
    {
        p.get("", "href=\"${'test'}\"");
        p.get("", "href=\"${'test'}\" & id=\"${'id'}\"");
        p.get("",
                " src=\"${'http://maps.google.com/maps/api/staticmap?&zoom=17&size=204x229&sensor=false&center='|lon|','|lat}\"");
    }

    @Inject
    RepMethodParserProvider p;
}
