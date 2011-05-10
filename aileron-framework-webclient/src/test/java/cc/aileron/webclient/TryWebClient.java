/**
 *
 */
package cc.aileron.webclient;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import cc.aileron.generic.util.SkipList;
import cc.aileron.junit.runner.guice.GuiceInjectRunner;
import cc.aileron.webclient.html.HtmlPage;
import cc.aileron.webclient.html.entity.HtmlAnchorElement;
import cc.aileron.webclient.html.entity.HtmlFormElement;

import com.google.inject.Inject;

/**
 * @author aileron
 */
@RunWith(GuiceInjectRunner.class)
public class TryWebClient
{

    /**
     * @throws HttpException
     * @throws IOException
     * @throws MalformedURLException
     * @throws SAXException 
     * @throws ParseException 
     * @throws URISyntaxException 
     */
    @Test
    public void cookie()
            throws MalformedURLException, IOException, HttpException,
            ParseException, SAXException, URISyntaxException
    {
        final WebResponse<HtmlPage> response = client.getPage(new WebRequestDefault()
        {
            @Override
            public URL url()
            {
                return url;
            }

            final URL url = new URL("http://aileron.cc/set-cookie.cgi");
        });
        System.out.println("--------------------------------------------");
        for (final HttpCookie cookie : response.cookies())
        {
            System.out.println(cookie.getName() + "=" + cookie.getValue());
        }
        System.out.println("--------------------------------------------");

        /*
         * jump
         */
        final WebResponse<HtmlPage> j = response.entity()
                .jump("/view-cookie.cgi");
        System.out.println("jump------------------------");
        System.out.println(j.entity());
        System.out.println(j.cookies());

        /*
         * anchor
         */
        final WebResponse<HtmlPage> a = response.entity()
                .<HtmlAnchorElement> getByXPath("//a")
                .get(0)
                .click();
        System.out.println("anchor----------------------");
        System.out.println(a.entity());
        System.out.println(a.cookies());

        /*
         * form
         */
        final WebResponse<HtmlPage> f = response.entity()
                .<HtmlFormElement> getByXPath("//form")
                .get(0)
                .submit(new SkipList<NameValuePair>());
        System.out.println("form-----------------------");
        System.out.println(f.entity());
        System.out.println(f.cookies());
    }

    /**
     * simple
     * @throws HttpException 
     * @throws IOException 
     * @throws MalformedURLException 
     */
    @Test
    public void simple()
            throws MalformedURLException, IOException, HttpException
    {
        final byte[] e = client.getBytes(new WebRequestDefault()
        {
            @Override
            public URL url()
            {
                return url;
            }

            final URL url = new URL("http://localhost/");
        }).entity();
        System.out.println(e);
    }

    /**
     * @throws HttpException
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     * @throws URISyntaxException
     */
    @Test
    public void spec()
            throws IOException, HttpException, ParseException, SAXException,
            URISyntaxException
    {
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("test", "hoge"));

        final URL uri = new URL("http://aileron.cc");
        final HtmlPage page = client.getPage(new WebRequest()
        {
            @Override
            public String charset()
            {
                return "SJIS";
            }

            @Override
            public List<HttpCookie> cookies()
            {
                return null;
            }

            @Override
            public WebRequestMethod method()
            {
                return WebRequestMethod.GET;
            }

            @Override
            public ArrayList<NameValuePair> params()
            {
                return params;
            }

            @Override
            public WebRequestProxySetting proxy()
            {
                return new WebRequestProxySetting()
                {
                    @Override
                    public String hostname()
                    {
                        return "localhost";
                    }

                    @Override
                    public int port()
                    {
                        return 8000;
                    }
                };
            }

            @Override
            public URL referer()
            {
                return null;
            }

            @Override
            public URL url()
            {
                return uri;
            }

            @Override
            public String userAgent()
            {
                return "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_3; ja-jp) AppleWebKit/531.22.7 (KHTML, like Gecko) Version/4.0.5 Safari/531.22.7";
            }
        })
                .entity();
        System.out.println(page.toString());

        final WebResponse<HtmlPage> res = page.<HtmlAnchorElement> getByXPath("//a")
                .get(0)
                .click();

        final HtmlPage page2 = res.entity();
        System.out.println(page2.toString());
        System.out.println(page2.getByXPath("//h2").get(0).text());

        final WebResponse<HtmlPage> res2 = page2.<HtmlAnchorElement> getByXPath("//a[@title='Pascal']")
                .get(0)
                .click();

        System.out.println(res2.toString());
    }

    @Inject
    WebClient client;
}
