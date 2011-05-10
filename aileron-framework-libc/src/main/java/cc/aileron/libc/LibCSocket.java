/**
 * 
 */
package cc.aileron.libc;

import java.nio.ByteBuffer;

import org.jruby.ext.posix.util.Platform;

import com.kenai.jaffl.annotations.In;
import com.kenai.jaffl.annotations.Out;
import com.kenai.jaffl.annotations.Transient;
import com.kenai.jaffl.byref.IntByReference;

/**
 * @author aileron
 *
 */
public interface LibCSocket
{
    /**
     * 
     * @author aileron
     *
     */
    final class BSDSockAddrUnix extends sockaddr_un
    {
        @Override
        public final int getFamily()
        {
            return sun_family.get();
        }

        @Override
        public final UTF8String path()
        {
            return sun_path;
        }

        @Override
        public final void setFamily(final int family)
        {
            sun_family.set((byte) family);
        }

        public BSDSockAddrUnix()
        {
        }

        /**
         * 
         */
        public final Signed8 sun_family = new Signed8();

        /**
         * 
         */
        public final Signed8 sun_len = new Signed8();

        /**
         * 
         */
        public final UTF8String sun_path = new UTF8String(LENGTH - 2);
    }

    /**
     * 
     * @author aileron
     *
     */
    final class DefaultSockAddrUnix extends sockaddr_un
    {
        @Override
        public final int getFamily()
        {
            return sun_family.get();
        }

        @Override
        public final UTF8String path()
        {
            return sun_path;
        }

        @Override
        public final void setFamily(final int family)
        {
            sun_family.set((short) family);
        }

        /**
         * 
         */
        public final Signed16 sun_family = new Signed16();

        /**
         * 
         */
        public final UTF8String sun_path = new UTF8String(LENGTH - 2);
    }

    /**
     * sockaddr_un structure
     */
    abstract class sockaddr_un extends com.kenai.jaffl.struct.Struct
    {
        /**
         * length
         */
        public final static int LENGTH = 106;

        /**
         * @return status new
         */
        public static final sockaddr_un newInstance()
        {
            return Platform.IS_BSD ? new LibCSocket.BSDSockAddrUnix()
                    : new DefaultSockAddrUnix();
        }

        /**
         * @return status family
         */
        public abstract int getFamily();

        /**
         * 
         * @return status path
         */
        public abstract UTF8String path();

        /**
         * @param family
         */
        public abstract void setFamily(int family);
    }

    /**
     * 
     * @param s
     * @param addr
     * @param addrlen
     * @return status
     */
    int accept(int s, @Transient sockaddr_un addr, IntByReference addrlen);

    /**
     * 
     * @param s
     * @param name
     * @param namelen
     * @return status
     */
    int bind(int s, @Transient sockaddr_un name, int namelen);

    /**
     * 
     * @param s
     * @return status
     */
    int close(int s);

    /**
     * 
     * @param s
     * @param name
     * @param namelen
     * @return status
     */
    int connect(int s, @In @Transient sockaddr_un name, int namelen);

    /**
     * 
     * @param fd
     * @param cmd
     * @param arg
     * @return status
     */
    int fcntl(int fd, int cmd, int arg);

    /**
     * 
     * @param s
     * @param addr
     * @param addrlen
     * @return status
     */
    int getpeername(int s, @Out @Transient sockaddr_un addr,
            IntByReference addrlen);

    /**
     * 
     * @param s
     * @param addr
     * @param addrlen
     * @return status
     */
    int getsockname(int s, @Out @Transient sockaddr_un addr,
            IntByReference addrlen);

    /**
     * 
     * @param s
     * @param level
     * @param optname
     * @param optval
     * @param optlen
     * @return status
     */
    int getsockopt(int s, int level, int optname, @Out byte[] optval,
            IntByReference optlen);

    /**
     * 
     * @param s
     * @param backlog
     * @return status
     */
    int listen(int s, int backlog);

    /**
     * 
     * @param arg
     */
    void perror(String arg);

    /**
     * 
     * @param s
     * @param buf
     * @param len
     * @param flags
     * @return status
     */
    int recv(int s, @Out ByteBuffer buf, int len, int flags);

    /**
     * 
     * @param s
     * @param buf
     * @param len
     * @param flags
     * @param from
     * @param fromlen
     * @return status
     */
    int recvfrom(int s, @Out ByteBuffer buf, int len, int flags,
            @Out @Transient sockaddr_un from, IntByReference fromlen);

    /**
     * 
     * @param s
     * @param msg
     * @param len
     * @param flags
     * @return status
     */
    int send(int s, @In ByteBuffer msg, int len, int flags);

    /**
     * 
     * @param s
     * @param level
     * @param optname
     * @param optval
     * @param optlen
     * @return status
     */
    int setsockopt(int s, int level, int optname, @In byte[] optval, int optlen);

    /**
     * 
     * @param s
     * @param how
     * @return status
     */
    int shutdown(int s, int how);

    /**
     * 
     * @param domain
     * @param type
     * @param protocol
     * @return status
     */
    int socket(int domain, int type, int protocol);

    /**
     * @param d
     * @param type
     * @param protocol
     * @param sv
     * @return status
     */
    int socketpair(int d, int type, int protocol, int[] sv);

    /**
     * @param path
     * @return status
     */
    int unlink(String path);

    /**
     * libcsocket
     */
    final LibCSocket libcsocket = com.kenai.jaffl.Library.loadLibrary(LibCSocket.class,
            Platform.IS_SOLARIS ? new String[] { "socket", "nsl", "c" }
                    : new String[] { "c" });

}
