/**
 *
 */
package cc.aileron.jetty;

import java.net.URL;
import java.security.ProtectionDomain;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author aileron
 */
public class JettyRunner
{
    /**
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception
    {
        final Options opt = new Options();
        opt.addOption("p", "port", true, "http port");
        opt.addOption("w", "warfile", true, "warfile");
        opt.addOption("c", "contextpath", true, "context path");

        final CommandLineParser parser = new PosixParser();
        final CommandLine cl;

        try
        {
            cl = parser.parse(opt, args);
        }
        catch (final ParseException e)
        {
            final HelpFormatter help = new HelpFormatter();
            help.printHelp("jetty", opt, true);
            return;
        }

        final String war = cl.getOptionValue('w');
        final int port = Integer.parseInt(cl.getOptionValue('p', "8080"));
        final String context = cl.getOptionValue('c', "/");

        new JettyRunner(port, context, war).run();
    }

    private void run() throws Exception
    {
        server.start();
        server.join();
    }

    /**
     * @param port
     * @param contextpath
     * @param rawwar
     */
    public JettyRunner(final int port, final String contextpath,
            final String rawwar)
    {
        final String war;
        if (rawwar.isEmpty())
        {
            final ProtectionDomain protectionDomain = JettyRunner.class.getProtectionDomain();
            final URL location = protectionDomain.getCodeSource().getLocation();
            war = location.toExternalForm() + "/WEB-INF/web.xml";
        }
        else
        {
            war = rawwar;
        }
        server = new Server(port);
        server.setHandler(new WebAppContext(war, contextpath));
    }

    private final Server server;
}