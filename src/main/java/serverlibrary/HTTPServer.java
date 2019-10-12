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
            String completeResponse = stringBuf.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param port
     * @author Tiancheng
     */
    public static void Listener(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    RequestReciever(socketChannel);
                }
            }
        } catch (IOException e) {
            System.out.println("HTTPServer: " + e.getMessage());
        }
    }

    /**
     * @param args
     * @author Tiancheng
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //usage: httpfs [-v] [-p PORT] [-d PATH-TO-DIR]
        //-v Prints debugging messages.

        //-d Specifies the directory that the server will use to read/write
        //requested files. Default is the current directory when launching the
        //application.
        System.out.println("Please enter startup parameters");
        String[] read = scanner.nextLine().toLowerCase().split(" ");
        scanner.close();

        int port = 8080;
        String directory = "src/server";

        if (read[0].equals("httpfs")) {

            for (int i = 0; i < read.length; i++) {
                if (read[i].equals("-v")) {
                    //-v Prints debugging messages.
                }

                if (read[i].equals("-p")) {
                    port = Integer.parseInt(read[i + 1]);
                }

                if (read[i].equals("-d")) {
                    directory = read[i + 1];
                }
            }

            Listener(port);
        } else {
            System.out.println("parameters invalid");
        }

        System.out.println("File server close");
    }
}