/**
 * 
 */
package cc.aileron.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

/**
 * @author aileron
 */
public interface BatchManager
{
    /**
     * @author aileron
     */
    class CmdLine
    {
        /**
         * @param procedure 
         * @throws Exception 
         */
        public static void run(final Procedure procedure) throws Exception
        {
            final BufferedReader stdReader = new BufferedReader(new InputStreamReader(System.in));
            for (String line; (line = stdReader.readLine()) != null;)
            {
                procedure.call(line);
            }
        }
    }

    /**
     * @author aileron
     */
    class Executor
    {
        /**
         * @param procedure
         * @throws Exception 
         */
        public static void apply(final Procedure procedure) throws Exception
        {
            apply(procedure, 1000, 5000);
        }

        /**
         * @param procedure
         * @param interval 
         * @param trouble 
         * @throws Exception 
         */
        public static void apply(final Procedure procedure, final int interval,
                final int trouble) throws Exception
        {
            switch (Mode.valueOf(System.getProperty("M", "Cmdline")))
            {
            case Server:
                new BatchManager.Server(Integer.getInteger("P"),
                        procedure,
                        interval,
                        trouble).run();
                return;

            case Cmdline:
                BatchManager.CmdLine.run(procedure);
                return;

            }
        }
    }

    /**
     * @author aileron
     */
    enum Mode
    {
        Cmdline, Server
    }

    /**
     * @author aileron
     */
    interface Procedure
    {
        /**
         * @param query
         * @throws IllegalArgumentException 入力されたクエリが不正だった場合のエラー
         * @throws Exception
         */
        void call(String query) throws IllegalArgumentException, Exception;
    }

    /**
     * @author aileron
     */
    class Server
    {
        static void LOG(final String message, final Object... argv)
        {
            System.err.printf("%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS.%1$tL\t",
                    System.currentTimeMillis());
            System.err.printf(message + "\n", argv);
        }

        /**
         * @throws IOException 
         */
        public void run() throws IOException
        {
            LOG("start server WaitTime(interval:%d,trouble:%d)",
                    wait.interval(),
                    wait.trouble());
            final ServerSocket server = new ServerSocket(port);
            for (;;)
            {
                final Socket client = server.accept();
                System.setOut(new PrintStream(client.getOutputStream()));

                final BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                try
                {
                    for (String line; (line = reader.readLine()) != null;)
                    {
                        if (!line.trim().isEmpty())
                        {
                            if (!line.trim().isEmpty())
                            {
                                LOG("try %s", line);
                                tryRun(line, 0);
                            }
                        }
                    }
                    client.shutdownInput();
                    client.shutdownOutput();
                    client.close();
                }
                catch (final SocketException e)
                {
                    client.shutdownInput();
                    client.shutdownOutput();
                    client.close();
                }
            }
        }

        private void tryRun(final String line, final int troubleCount)
        {
            try
            {
                final long time = System.currentTimeMillis();
                procedure.call(line);
                final long processdTime = System.currentTimeMillis() - time;
                final long interval = wait.interval() - processdTime;
                if (interval > 0)
                {
                    LOG("wait interval %d", interval);
                    Thread.sleep(interval);
                }
                processedCount += 1;

                if (processedCount % 100 == 0)
                {
                    final long tmp = System.currentTimeMillis();
                    LOG("100 call is processed at %d msec", (tmp - counter));
                    counter = tmp;
                }
            }
            catch (final IllegalArgumentException e)
            {
                LOG("IllegalArgument %s", line);
                return;
            }
            catch (final Exception e)
            {
                LOG("trouble at %d to sleep %d msec",
                        troubleCount,
                        wait.trouble());
                LOG("Throwable e");
                e.printStackTrace(System.err);
                try
                {
                    Thread.sleep(wait.trouble());

                    if (troubleCount == 0)
                    {
                        /*
                         * エラーが発生した為、インターバルを長くする
                         */
                        wait.interval((int) (wait.interval() * 1.5));
                    }

                    /*
                     * trouble wait time add
                     */
                    wait.trouble((int) (wait.trouble() * 1.3));

                    LOG("up WaitTime(interval:%d,trouble:%d)",
                            wait.interval(),
                            wait.trouble());

                    /*
                     * retry
                     */
                    tryRun(line, troubleCount + 1);
                }
                catch (final InterruptedException ie)
                {
                    throw new Error(ie);
                }
            }
        }

        /**
         * @param port
         * @param procedure
         * @param defaultInterval
         * @param defaultTrouble
         * @throws IOException 
         */
        public Server(final int port, final Procedure procedure,
                final int defaultInterval, final int defaultTrouble)
                throws IOException
        {
            this.port = port;
            this.procedure = procedure;
            this.wait = new WaitTime()
            {
                @Override
                public int interval()
                {
                    return interval;
                }

                @Override
                public void interval(final int interval)
                {
                    this.interval = interval;
                    saveStatus();
                }

                @Override
                public int trouble()
                {
                    return trouble;
                }

                @Override
                public void trouble(final int trouble)
                {
                    this.trouble = trouble;
                    saveStatus();
                }

                /**
                 * save
                 */
                private void saveStatus()
                {
                    buffer.putInt(0, interval);
                    buffer.putInt(4, trouble);
                }

                final MappedByteBuffer buffer;
                int interval;
                int trouble;
                {
                    buffer = new RandomAccessFile("/tmp/"
                            + procedure.getClass().getName(), "rw").getChannel()
                            .map(MapMode.READ_WRITE, 0, 1024);

                    interval = buffer.getInt(0);
                    trouble = buffer.getInt(4);

                    interval = interval == 0 ? defaultInterval : interval;
                    trouble = trouble == 0 ? defaultTrouble : trouble;
                }
            };
        }

        long counter = System.currentTimeMillis();
        final int port;
        final Procedure procedure;
        int processedCount;
        final WaitTime wait;
    }

    /**
     * @author aileron
     */
    interface WaitTime
    {
        /**
         * @return interval
         */
        int interval();

        /**
         * @param interval
         */
        void interval(int interval);

        /**
         * @return trouble
         */
        int trouble();

        /**
         * @param trouble
         */
        void trouble(int trouble);
    }

    /**
     * out
     */
    final PrintStream out = System.out;
}