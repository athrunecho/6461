package serverlibrary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * A file server based on HTTP protocol
 */
public class HTTPServer {

    static int port = 8080;
    static String directory = "src/server";

    /**
     * read request
     *
     * @param channel socket channel from listener
     * @author Tiancheng
     */
    private static void RequestReciever(SocketChannel channel) {
        try {
            ByteBuffer buf = ByteBuffer.allocate(2048);
            StringBuffer stringBuf = new StringBuffer();

            channel.read(buf);
            buf.flip();

            while (buf.hasRemaining()) {
                stringBuf.append((char) buf.get());
            }

            String completeRequest = stringBuf.toString().trim();

            System.out.println(completeRequest);

            String pkg = Parser.Parse(completeRequest);
            send(channel, pkg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param port
     * @author Tiancheng
     */
    private static void Listener(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    Log.logger.info("Socket has been built with " + socketChannel.getRemoteAddress());
                    RequestReciever(socketChannel);
                }
            }
        } catch (IOException e) {
            System.out.println("HTTPServer: " + e.getMessage());
        }
    }


    private static void send(SocketChannel channel, String data) {
        try {
            if (data == null) {
                return;
            }
            // Using ByteBuffer to write into socket channel
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            System.out.println(data);
            buffer.put(data.getBytes());
            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @author Tiancheng
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //-d Specifies the directory that the server will use to read/write
        //requested files. Default is the current directory when launching the
        //application.
        System.out.println("Please enter startup parameters");
        String[] read = scanner.nextLine().toLowerCase().split(" ");
        scanner.close();

        if (read[0].equals("httpfs")) {

            for (int i = 0; i < read.length; i++) {
                if (read[i].equals("-v")) {
                    Log.initial();
                    Log.logger.info("debugging messages open");
                }

                if (read[i].equals("-p")) {
                    port = Integer.parseInt(read[i + 1]);
                }

                if (read[i].equals("-d")) {
                    directory = read[i + 1];
                }
            }

            Log.logger.info("listening to the port of " + port);
            Log.logger.info("StartUp path is " + directory);

            //StartUp
            Listener(port);
        } else {
            System.out.println("parameters invalid");
        }

        System.out.println("File server close");
    }
}