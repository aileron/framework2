/**
 * 
 */
package cc.aileron.libc;

/**
 * @author aileron
 */
public interface LibCFifo
{
    /**
     * @param filename
     * @param mode
     * @return status
     */
    int mkfifo(String filename, int mode);

    /**
     * libcfifo
     */
    final LibCFifo libcfifo = com.kenai.jaffl.Library.loadLibrary(LibCFifo.class,
            "c");
}
