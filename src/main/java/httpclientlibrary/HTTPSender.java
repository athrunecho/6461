package httpclientlibrary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * HTTPSender class sends data through SocketChannel
 */
public class HTTPSender {

    /**
     * Send method sends request in bytes to object server
     * @author Anqi Wang
     * @param channel alive SocketChannel
     * @param data request data
     */
    public static void send(SocketChannel channel, String data) {
        try{
            if(data == null){
                return;
            }
            // Using ByteBuffer to write into socket channel
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            buffer.put(data.getBytes());
            buffer.flip();

            while(buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}