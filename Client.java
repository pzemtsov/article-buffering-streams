import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;


public class Client
{
    private static ByteBuffer buf = ByteBuffer.allocateDirect (1024*1024);
    
    public static void main (String [] args) throws Exception
    {
        String hostName = args [0];
        final SocketAddress socketAddr = new InetSocketAddress (hostName, 22222);
        SocketChannel chan = SocketChannel.open ();
        chan.connect (socketAddr);

        byte[] type = new byte [4];
        byte[] msg = new byte [1024];
        buf.limit (0);
        
        while (true) {
            long t0 = System.currentTimeMillis ();
            long sum = 0;
            int N = 10000000;
            for (int i = 0; i < N; i++) {
                ensure (4, chan);
                buf.get (type);
                ensure (4, chan);
                int len = buf.getInt ();
                ensure (len, chan);
                buf.get (msg, 0, len);
                processMessage (type, msg, len);
                sum += len + 8;
            }
            long t1 = System.currentTimeMillis ();
            long t = t1 - t0;
            System.out.printf ("Time for %d msg: %d; speed: %d msg/s; %.1f MB/s\n",
                               N, t, N * 1000L / t, sum * 0.001 / t);
        }
    }

    private static void ensure (int len, ByteChannel chan) throws IOException
    {
        if (buf.remaining () < len) {
            buf.compact ();
            buf.flip ();
            do {
                buf.position (buf.limit ());
                buf.limit (buf.capacity ());
                chan.read (buf);
                buf.flip ();
            } while (buf.remaining () < len);
        }
    }
    
    private static void processMessage (byte [] type, byte [] msg, int len)
    {
    }
}
