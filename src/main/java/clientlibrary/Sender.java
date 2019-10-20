package clientlibrary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Sender class sends data through SocketChannel
 */
public class Sender {

    /**
     * Send method sends request in bytes to object server
     *
     * @param channel alive SocketChannel
     * @param data    request data
     * @author Anqi Wang
     */
    public static void send(SocketChannel channel, String data) {
        try {
            if (data == null) {
                return;
            }
            // Using ByteBuffer to write into socket channel
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            System.out.println("\n" + data);
            buffer.put(data.getBytes());
            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}