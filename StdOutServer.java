import java.io.OutputStream;
import java.nio.ByteBuffer;


public class StdOutServer
{
    public static void main (String [] args) throws Exception
    {
        byte [] MESSAGE_TYPE = new byte[] {1, 2, 3, 4};
        int MESSAGE_LEN = 100;
        int N = 1024;

        byte [] buf = new byte [(4 + 4 + MESSAGE_LEN) * N];
        ByteBuffer buffer = ByteBuffer.wrap (buf);
        int num = 0;
        for (int i = 0; i < N; i++) {
            buffer.put (MESSAGE_TYPE);
            buffer.putInt (MESSAGE_LEN);
            for (int j = 0; j < MESSAGE_LEN; j++) {
                buffer.put ((byte) num);
                num += 3;
            }
        }

        OutputStream out = System.out;
        while (true) {
            out.write (buf);
        }
    }
}
