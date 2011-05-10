/**
 * 
 */
package cc.aileron.wsgi.ga;

import static cc.aileron.wsgi.context.WsgiContextProvider.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.aileron.commons.resource.ResourceNotFoundException;
import cc.aileron.commons.util.ResourceUtils;
import cc.aileron.workflow.phase.DisposePhase;
import cc.aileron.workflow.phase.DisposePhaseError;

import com.google.inject.Singleton;

/**
 * @author aileron
 */
@Singleton
public class GoogleAnalyticsImage implements DisposePhase
{
    private static final String COOKIE_NAME = "__utmmobile";

    // The path the cookie will be available to, edit this to use a different
    // cookie path.
    private static final String COOKIE_PATH = "/";

    // Two years in seconds.
    private static final int COOKIE_USER_PERSISTENCE = 63072000;

    /**
    Copyright 2009 Google Inc. All Rights Reserved.
    **/

    // Tracker version.
    private static final String version = "4.4sj";

    // The last octect of the IP address is removed to anonymize the user.
    private static String getIP(final String remoteAddress)
    {
        if (isEmpty(remoteAddress))
        {
            return "";
        }
        // Capture the first three octects of the IP address and replace the
        // forth
        // with 0, e.g. 124.455.3.123 becomes 124.455.3.0
        final String regex = "^([^.]+\\.[^.]+\\.[^.]+\\.).*";
        final Pattern getFirstBitOfIPAddress = Pattern.compile(regex);
        final Matcher m = getFirstBitOfIPAddress.matcher(remoteAddress);
        if (m.matches())
        {
            return m.group(1) + "0";
        }
        return "";
    }

    // Get a random number string.
    private static String getRandomNumber()
    {
        return Integer.toString((int) (Math.random() * 0x7fffffff));
    }

    // Generate a visitor id for this hit.
    // If there is a visitor id in the cookie, use that, otherwise
    // use the guid if we have one, otherwise use a random number.
    private static String getVisitorId(final String guid, final String account,
            final String userAgent, final Cookie cookie)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {

        // If there is a value in the cookie, don't change it.
        if (cookie != null && cookie.getValue() != null)
        {
            return cookie.getValue();
        }

        String message;
        if (!isEmpty(guid))
        {
            // Create the visitor id using the guid.
            message = guid + account;
        }
        else
        {
            // otherwise this is a new user, create a new random id.
            message = userAgent + getRandomNumber()
                    + UUID.randomUUID().toString();
        }

        final MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(message.getBytes("UTF-8"), 0, message.length());
        final byte[] sum = m.digest();
        final BigInteger messageAsNumber = new BigInteger(1, sum);
        String md5String = messageAsNumber.toString(16);

        // Pad to make sure id is 32 characters long.
        while (md5String.length() < 32)
        {
            md5String = "0" + md5String;
        }

        return "0x" + md5String.substring(0, 16);
    }

    // A string is empty in our terms, if it is null, empty or a dash.
    private static boolean isEmpty(final String in)
    {
        return in == null || "-".equals(in) || "".equals(in);
    }

    @Override
    public void dispose() throws DisposePhaseError, Exception
    {
        final HttpServletRequest request = context().request();
        final HttpServletResponse response = context().response();
        trackPageView(request, response);
    }

    // Make a tracking request to Google Analytics from this server.
    // Copies the headers from the original request to the new one.
    // If request containg utmdebug parameter, exceptions encountered
    // communicating with Google Analytics are thown.
    private void sendRequestToGoogleAnalytics(final String utmUrl,
            final HttpServletRequest request) throws Exception
    {
        try
        {
            final URL url = new URL(utmUrl);
            final URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            connection.addRequestProperty("User-Agent",
                    request.getHeader("User-Agent"));
            connection.addRequestProperty("Accepts-Language",
                    request.getHeader("Accepts-Language"));
            connection.getInputStream();
        }
        catch (final Exception e)
        {
            if (request.getParameter("utmdebug") != null)
            {
                throw new Exception(e);
            }
        }
    }

    // Track a page view, updates all the cookies and campaign tracker,
    // makes a server side request to Google Analytics and writes the
    // transparent
    // gif byte data to the response.
    private void trackPageView(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception
    {
        String domainName = request.getServerName();
        if (isEmpty(domainName))
        {
            domainName = "";
        }

        // Get the referrer from the utmr parameter, this is the referrer to the
        // page that contains the tracking pixel, not the referrer for tracking
        // pixel.
        String documentReferer = request.getParameter("utmr");
        if (isEmpty(documentReferer))
        {
            documentReferer = "-";
        }
        else
        {
            documentReferer = URLDecoder.decode(documentReferer, "UTF-8");
        }
        String documentPath = request.getParameter("utmp");
        if (isEmpty(documentPath))
        {
            documentPath = "";
        }
        else
        {
            documentPath = URLDecoder.decode(documentPath, "UTF-8");
        }

        final String account = request.getParameter("utmac");
        String userAgent = request.getHeader("User-Agent");
        if (isEmpty(userAgent))
        {
            userAgent = "";
        }

        // Try and get visitor cookie from the request.
        final Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if (cookies != null)
        {
            for (int i = 0; i < cookies.length; i++)
            {
                if (cookies[i].getName().equals(COOKIE_NAME))
                {
                    cookie = cookies[i];
                }
            }
        }

        String guidHeader = request.getHeader("X-DCMGUID");
        if (isEmpty(guidHeader))
        {
            guidHeader = request.getHeader("X-UP-SUBNO");
        }
        if (isEmpty(guidHeader))
        {
            guidHeader = request.getHeader("X-JPHONE-UID");
        }
        if (isEmpty(guidHeader))
        {
            guidHeader = request.getHeader("X-EM-UID");
        }

        final String visitorId = getVisitorId(guidHeader,
                account,
                userAgent,
                cookie);

        // Always try and add the cookie to the response.
        final Cookie newCookie = new Cookie(COOKIE_NAME, visitorId);
        newCookie.setMaxAge(COOKIE_USER_PERSISTENCE);
        newCookie.setPath(COOKIE_PATH);
        response.addCookie(newCookie);

        final String utmGifLocation = "http://www.google-analytics.com/__utm.gif";

        // Construct the gif hit url.
        final String utmUrl = utmGifLocation + "?" + "utmwv=" + version
                + "&utmn=" + getRandomNumber() + "&utmhn="
                + URLEncoder.encode(domainName, "UTF-8") + "&utmr="
                + URLEncoder.encode(documentReferer, "UTF-8") + "&utmp="
                + URLEncoder.encode(documentPath, "UTF-8") + "&utmac="
                + account + "&utmcc=__utma%3D999.999.999.999.999.1%3B"
                + "&utmvid=" + visitorId + "&utmip="
                + getIP(request.getRemoteAddr());

        sendRequestToGoogleAnalytics(utmUrl, request);

        // If the debug parameter is on, add a header to the response that
        // contains
        // the url that was used to contact Google Analytics.
        if (request.getParameter("utmdebug") != null)
        {
            response.setHeader("X-GA-MOBILE-URL", utmUrl);
        }
        // Finally write the gif data to the response.
        writeGifData(response);
    }

    // Writes the bytes of a 1x1 transparent gif into the response.
    private void writeGifData(final HttpServletResponse response)
            throws IOException
    {
        response.addHeader("Cache-Control",
                "private, no-cache, no-cache=Set-Cookie, proxy-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "Wed, 17 Sep 1975 21:32:10 GMT");
        response.addHeader("Content-Type", "image/gif");
        response.addHeader("Content-Length", "43");
        final ServletOutputStream output = response.getOutputStream();
        output.write(blankGif);
        output.flush();
    }

    /**
     * @throws ResourceNotFoundException 
     * @throws IOException 
     */
    public GoogleAnalyticsImage() throws IOException, ResourceNotFoundException
    {
        blankGif = ResourceUtils.resource("blank.gif").toBytes();
    }

    private final byte[] blankGif;
}
