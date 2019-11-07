package clientlibrary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Multiple Client test
 */
public class MultiClients {

    /**
     * Read Method
     *
     * @param address server address
     */
    private static void ReadClient(SocketAddress address) {
        try {
            ByteBuffer buffer1 = ByteBuffer.allocate(1024);
            ByteBuffer buffer2 = ByteBuffer.allocate(1024);
            SocketChannel channel = SocketChannel.open();
            channel.connect(address);

            String data = "GET /test.txt HTTP/1.0\r\n" +
                    "Host:localhost\r\n" +
                    "Connection:Keep-Alive\r\n\r\n";

            buffer1.put(data.getBytes());
            buffer1.flip();

            while (buffer1.hasRemaining()) {
                channel.write(buffer1);
            }
            buffer1.clear();

            StringBuffer stringBuf = new StringBuffer();

            channel.read(buffer2);
            buffer2.flip();

            while (buffer2.hasRemaining()) {
                stringBuf.append((char) buffer2.get());
            }
            buffer2.clear();

            String completeResponse = stringBuf.toString().trim();
            System.out.println(completeResponse + "\n");

            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write method
     *
     * @param address server address
     */
    private static void WriteClient(SocketAddress address) {
        try {
            ByteBuffer buffer1 = ByteBuffer.allocate(1024);
            ByteBuffer buffer2 = ByteBuffer.allocate(1024);
            SocketChannel channel = SocketChannel.open();
            channel.connect(address);

            String data = "POST /test.txt HTTP/1.0\r\n" +
                    "Host:localhost\r\n" +
                    "OverWrite:false\r\n" +
                    "Content-Length:6\r\n" +
                    "Connection:Keep-Alive\r\n" +
                    "\r\n" +
                    "write";

            String mod = data + "\r\n";
            buffer1.put(mod.getBytes());
            buffer1.flip();

            while (buffer1.hasRemaining()) {
                channel.write(buffer1);
            }
            buffer1.clear();

            StringBuffer stringBuf = new StringBuffer();

            channel.read(buffer2);
            buffer2.flip();

            while (buffer2.hasRemaining()) {
                stringBuf.append((char) buffer2.get());
            }
            buffer2.clear();
            String completeResponse = stringBuf.toString().trim();

            System.out.println(completeResponse + "\n");


            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        InetSocketAddress localhost = new InetSocketAddress("localhost", 8080);

        Runnable Write = () -> {
            try {
                WriteClient(localhost);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Runnable Read = () -> {
            try {
                ReadClient(localhost);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < 5; i++) {
            new Thread(Read).start();
            new Thread(Write).start();
        }
    }
}
