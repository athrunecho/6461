package clientlibrary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 */
public class MultiClients {

    /**
     * @param address
     */
    private static void ReadClient(SocketAddress address) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.connect(address);

            String data = "GET /test.txt HTTP/1.0\r\n" +
                    "Host:localhost\r\n" +
                    "Connection:Keep-Alive\r\n\r\n";

            //loop 5 times
            for(int i=0;i<5;i++){
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                buffer.put(data.getBytes());
                buffer.flip();

                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                buffer.clear();

                StringBuffer stringBuf = new StringBuffer();

                channel.read(buffer);
                buffer.flip();

                while (buffer.hasRemaining()) {
                    stringBuf.append((char) buffer.get());
                }
                buffer.clear();

                String completeResponse = stringBuf.toString().trim();
                System.out.println(completeResponse + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param address
     */
    private static void WriteClient(SocketAddress address) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.connect(address);

            String data = "POST /test.txt HTTP/1.0\r\n" +
                    "Host:localhost\r\n" +
                    "OverWrite:false\r\n" +
                    "Content-Length:6\r\n" +
                    "Connection:Keep-Alive\r\n"+
                    "\r\n" +
                    "write";

            //loop 5 times
            for(int i=0;i<5;i++){
                String mod = data + String.valueOf(i) + "\r\n";
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                buffer.put(mod.getBytes());
                buffer.flip();

                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                buffer.clear();

                StringBuffer stringBuf = new StringBuffer();

                channel.read(buffer);
                buffer.flip();

                while (buffer.hasRemaining()) {
                    stringBuf.append((char) buffer.get());
                }
                buffer.clear();
                String completeResponse = stringBuf.toString().trim();

                System.out.println(completeResponse + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
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

        Thread Readthread = new Thread(Read);
        Thread Writethread = new Thread(Write);
        Readthread.start();
        Writethread.start();
    }
}
