/**
 * 
 */
package cc.aileron.libc;

import static cc.aileron.libc.LibCSocket.*;
import static com.kenai.constantine.platform.AddressFamily.*;
import static com.kenai.constantine.platform.Sock.*;

import java.nio.ByteBuffer;

import com.kenai.jaffl.byref.IntByReference;

/**
 * @author aileron
 */
public class TryLibCSocket
{
    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        final LibCSocket.sockaddr_un serveraddr = LibCSocket.sockaddr_un.newInstance();
        serveraddr.setFamily(AF_UNIX.value());
        serveraddr.path().set("/tmp/socket");

        final int server = libcsocket.socket(AF_UNIX.value(),
                SOCK_STREAM.value(),
                0);

        libcsocket.bind(server, serveraddr, LibCSocket.sockaddr_un.LENGTH);
        libcsocket.listen(server, 5);

        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        final LibCSocket.sockaddr_un clientaddr = LibCSocket.sockaddr_un.newInstance();
        for (;;)
        {
            final int client = libcsocket.accept(server,
                    clientaddr,
                    new IntByReference(LibCSocket.sockaddr_un.LENGTH));

            while (read(client, buffer) > 0)
            {
            }
            System.out.println(buffer);
            libcsocket.close(client);
        }
    }

    private static int read(final int fd, final ByteBuffer dst)
    {
        final int max = dst.remaining();
        final int v = libcsocket.recv(fd, dst, max, 0);
        if (v <= 0)
        {
            return -1;
        }
        dst.position(dst.position() + v);
        return v;
    }
}
