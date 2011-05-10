/**
 *
 */
package cc.aileron.proxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aileron
 */
public class ProxyHandler extends IoHandlerAdapter
{
    protected static final String OTHER_IO_SESSION = AbstractProxyIoHandler.class.getName()
            + ".OtherIoSession";
    static final Charset CHARSET = Charset.forName("iso8859-1");

    final static Logger LOGGER = LoggerFactory.getLogger(AbstractProxyIoHandler.class);

    @Override
    public void messageReceived(final IoSession session, final Object message)
            throws Exception
    {
        final IoBuffer rb = (IoBuffer) message;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(rb.asInputStream()));
        final String methodcommand = reader.readLine();
        final HashMap<String, String> parameters = new HashMap<String, String>();
        for (String line = reader.readLine(); line != null
                && line.isEmpty() == false; line = reader.readLine())
        {
            final String[] token = line.split(": ");
            parameters.put(token[0], token[1]);
        }

        final String[] host = parameters.get("Host").split(":");
        final String hostname = host[0];
        final int port = host.length > 1 ? Integer.parseInt(host[1]) : 80;

        final IoConnector connector = new NioSocketConnector();
        connector.setHandler(serverToProxyIoHandler);
        connector.connect(new InetSocketAddress(hostname, port))
                .addListener(new IoFutureListener<IoFuture>()
                {
                    @Override
                    public void operationComplete(final IoFuture future)
                    {
                        try
                        {
                            future.getSession().setAttribute(OTHER_IO_SESSION,
                                    session);
                            session.setAttribute(OTHER_IO_SESSION,
                                    future.getSession());
                            final IoSession session2 = future.getSession();
                            session2.resumeRead();
                            session2.resumeWrite();
                        }
                        catch (final RuntimeIoException e)
                        {
                            // Connect failed
                            session.close(true);
                        }
                        finally
                        {
                            session.resumeRead();
                            session.resumeWrite();
                        }
                    }
                });
    }

    @Override
    public void sessionClosed(final IoSession session) throws Exception
    {
        if (session.getAttribute(OTHER_IO_SESSION) != null)
        {
            final IoSession sess = (IoSession) session.getAttribute(OTHER_IO_SESSION);
            sess.setAttribute(OTHER_IO_SESSION, null);
            sess.close(false);
            session.setAttribute(OTHER_IO_SESSION, null);
        }
    }

    @Override
    public void sessionCreated(final IoSession session) throws Exception
    {
    }

    @Override
    public void sessionOpened(final IoSession session) throws Exception
    {
    }

    private final IoHandlerAdapter serverToProxyIoHandler = new IoHandlerAdapter()
    {
        @Override
        public void messageReceived(final IoSession session,
                final Object message) throws Exception
        {
            final IoBuffer rb = (IoBuffer) message;
            final IoBuffer wb = IoBuffer.allocate(rb.remaining());
            rb.mark();
            wb.put(rb);
            wb.flip();
            ((IoSession) session.getAttribute(OTHER_IO_SESSION)).write(wb);
            rb.reset();
            LOGGER.info(rb.getString(CHARSET.newDecoder()));
        }

        @Override
        public void sessionClosed(final IoSession session) throws Exception
        {
            if (session.getAttribute(OTHER_IO_SESSION) != null)
            {
                final IoSession sess = (IoSession) session.getAttribute(OTHER_IO_SESSION);
                sess.setAttribute(OTHER_IO_SESSION, null);
                sess.close(false);
                session.setAttribute(OTHER_IO_SESSION, null);
            }
        }

        @Override
        public void sessionCreated(final IoSession session) throws Exception
        {
            session.suspendRead();
            session.suspendWrite();
        }
    };
}
