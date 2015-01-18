import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class Client
{
    public static void main (String [] args) throws Exception
    {
        String hostName = args [0];
        Socket socket = new Socket (hostName, 22222);
        InputStream in = socket.getInputStream ();
        in = new BufferedInputStream (in, 1024*1024);
        while (true) {
            long t0 = System.currentTimeMillis ();
            long sum = 0;
            int N = 10000000;
            for (int i = 0; i < N; i++) {
                byte [] type = readBytes (in, 4);
                int len = readInt (in);
                byte [] msg = readBytes (in, len);
                processMessage (type, msg);
                sum += msg.length + 8;
            }
            long t1 = System.currentTimeMillis ();
            long t = t1 - t0;
            System.out.printf ("Time for %d msg: %d; speed: %d msg/s; %.1f MB/s\n",
                               N, t, N * 1000L / t, sum * 0.001 / t);
        }
    }
    
    private static byte [] readBytes (InputStream in, int expectedSize) throws IOException
    {
        final byte[] buffer = new byte[expectedSize];
        int totalReadSize = 0;
        while (totalReadSize < expectedSize) {
            int readSize = in.read(buffer, totalReadSize, expectedSize - totalReadSize);
            if (readSize < 0) throw new EOFException ();
            totalReadSize += readSize;
        }
        return buffer;
    }
    
    private static final int readInt (InputStream in) throws IOException
    {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    private static void processMessage (byte [] type, byte [] msg)
    {
    }
}
