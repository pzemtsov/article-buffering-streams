import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class Server
{
    public static void main (String [] args) throws Exception
    {
        byte [] MESSAGE_TYPE = new byte[] {1, 2, 3, 4};
        int MESSAGE_LEN = 100;
        int N = 1024;
        byte [] message = new byte [MESSAGE_LEN];

        ByteBuffer buffer = ByteBuffer.allocateDirect ((4 + 4 + MESSAGE_LEN) * N);
        int num = 0;
        for (int i = 0; i < N; i++) {
            buffer.put (MESSAGE_TYPE);
            buffer.putInt (MESSAGE_LEN);
            for (int j = 0; j < MESSAGE_LEN; j++) {
                buffer.put ((byte) num);
                num += 3;
            }
        }
        
        ServerSocketChannel serverChannel = ServerSocketChannel.open ();
        serverChannel.socket ().bind (new InetSocketAddress (22222));
        SocketChannel channel = serverChannel.accept ();
        while (true) {
            int len = channel.write (buffer);
            buffer.flip ();
        }
    }
}
