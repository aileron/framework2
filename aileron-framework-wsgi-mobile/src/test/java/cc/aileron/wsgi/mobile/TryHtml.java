package cc.aileron.wsgi.mobile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cc.aileron.webclient.WebClient;
import cc.aileron.webclient.WebRequestDefault;
import cc.aileron.webclient.html.entity.HtmlElement;

import com.google.inject.Guice;

/**
 * html パース
 * 
 * @author aileron
 */
public class TryHtml
{
    /**
     * @param args
     * @throws ParseException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     * @throws SAXException
     */
    public static void main(final String[] args)
            throws ParseException, MalformedURLException, IOException,
            HttpException, SAXException
    {
        final WebClient web = Guice.createInjector()
                .getInstance(WebClient.class);

        final List<HtmlElement> trs = web.getPage(new WebRequestDefault()
        {
            @Override
            public URL url()
            {
                return url;
            }

            final URL url = new URL("http://www.unicode.org/~scherer/emoji4unicode/snapshot/full.html");
        })
                .entity()
                .getByXPath("//tr[@id]");

        for (final HtmlElement tr : trs)
        {
            final List<HtmlElement> tds = tr.getByXPath("td");
            final NodeList dIs = tds.get(3).raw().getChildNodes();
            final NodeList kIs = tds.get(4).raw().getChildNodes();
            final NodeList sIs = tds.get(5).raw().getChildNodes();

            final String du = u(dIs.item(5));
            final String ds = s(dIs.item(7));

            final String ku = u(kIs.item(5));
            final String ks = s(kIs.item(7));

            final String su = u(sIs.item(5));
            final String ss = s(sIs.item(7));

            System.out.println(",{" + q(ds) + "," + q(ks) + "," + q(ss) + "}");
            // System.out.println(du + "," + ku + "," + su);
        }
    }

    private static String q(final String v)
    {
        return "\"" + v + "\"";
    }

    private static String s(final Node node)
    {
        if (node == null)
        {
            return "";
        }
        final String text = node.getTextContent();
        if (text == null || text.isEmpty())
        {
            return "";
        }
        return text.substring(5, 9);
    }

    private static String u(final Node node)
    {
        if (node == null)
        {
            return "";
        }
        final String text = node.getTextContent();
        if (text == null || text.isEmpty())
        {
            return "";
        }
        return text.substring(2);
    }
}
