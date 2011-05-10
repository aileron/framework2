/**
 *
 */
package cc.aileron.proxy;

import org.apache.mina.core.session.IoSession;

/**
 * @author aileron
 */
public class ClientToProxyIoHandler extends AbstractProxyIoHandler
{
    @Override
    public void sessionOpened(final IoSession session) throws Exception
    {

        /*
         * final InetSocketAddress remoteAddress = null;
         * connector.connect(remoteAddress) .addListener(new
         * IoFutureListener<ConnectFuture>() { public void
         * operationComplete(final ConnectFuture future) { try {
         * future.getSession().setAttribute(OTHER_IO_SESSION, session);
         * session.setAttribute(OTHER_IO_SESSION, future.getSession()); final
         * IoSession session2 = future.getSession(); session2.resumeRead();
         * session2.resumeWrite(); } catch (final RuntimeIoException e) { //
         * Connect failed session.close(true); } finally { session.resumeRead();
         * session.resumeWrite(); } } });
         */
    }

    /**
     */
    public ClientToProxyIoHandler()
    {
    }

    private final ServerToProxyIoHandler connectorHandler = new ServerToProxyIoHandler();
}
