/**
 *
 */
package cc.aileron.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * @author aileron
 */
public class ProxyServer
{
    /**
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException
    {
        final int listenport = Integer.parseInt(System.getProperty("port",
                "10084"));
        final NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(new ProxyHandler());
        acceptor.bind(new InetSocketAddress(listenport));

        System.out.println("Listening on port " + listenport);

    }
}
