import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class StdOutServer
{
    public static void main (String [] args) throws Exception
    {
        byte [] MESSAGE_TYPE = new byte[] {1, 2, 3, 4};
        int MESSAGE_LEN = 100;
        int N = 1024;
        byte [] message = new byte [MESSAGE_LEN];

        ByteBuffer buffer = ByteBuffer.allocateDirect ((4 + 4 + MESSAGE_LEN) * N);
        for (int i = 0; i < N; i++) {
            buffer.put (MESSAGE_TYPE);
            buffer.putInt (MESSAGE_LEN);
            buffer.put (message);
        }
        
        FileChannel channel = new FileOutputStream ("/proc/self/fd/1").getChannel ();
        while (true) {
            channel.write (buffer);
            buffer.flip ();
        }
    }
}
