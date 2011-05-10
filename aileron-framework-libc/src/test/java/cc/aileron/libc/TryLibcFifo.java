/**
 * 
 */
package cc.aileron.libc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author aileron
 *
 */
public class TryLibcFifo
{
    /**
     * @param args
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void main(final String[] args)
            throws IOException, InterruptedException
    {
        LibCFifo.libcfifo.mkfifo("/tmp/fifo", 0755);

        final File fifo = new File("/tmp/fifo");
        for (;;)
        {
            final BufferedReader queryReader = new BufferedReader(new FileReader(fifo));
            final BufferedWriter queryWriter = new BufferedWriter(new FileWriter(fifo));
            for (String line; (line = queryReader.readLine()) != null;)
            {
                queryWriter.write("ok : " + line);
                System.out.println(line + " start");
                Thread.sleep(5000);
                System.out.println(line + " end");
            }
        }
    }

}
