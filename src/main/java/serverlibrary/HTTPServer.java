package serverlibrary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

/**
 * A file server based on HTTP protocol
 */
public class HTTPServer {

    static String directory = "src/server";
    private static int port = 8080;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter startup parameters");
        String[] read = scanner.nextLine().toLowerCase().split(" ");
        scanner.close();

        if (read[0].equals("httpfs")) {

            Log.hide();

            for (int i = 0; i < read.length; i++) {
                if (read[i].contains("-v")) {
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
            new HTTPServer().Listener(port);
        } else {
            System.out.println("parameters invalid");
        }

        System.out.println("File server close");
    }

    /**
     * readAndEcho is the method for reply to client and close the socket
     * @param sk The select key which bind the socket
     */
    private void readAndEcho(SelectionKey sk) {
        SocketChannel client = (SocketChannel) sk.channel();
        try {
            ByteBuffer buf = ByteBuffer.allocate(2048);
            while (true) {

                int n = client.read(buf);

                if (n == -1) {
                    unregister(sk);
                    return;
                }
                if (n == 0) {
                    return;
                }

                String completeRequest = new String(buf.array()).trim();
                System.out.println(completeRequest + "\n");
                String pkg = Parser.Parse(completeRequest);

                buf.flip();
                // Using ByteBuffer to write into socket channel
                buf = ByteBuffer.wrap(pkg.getBytes());
                client.write(buf);
                buf.clear();
            }
        } catch (IOException e) {
            unregister(sk);
            Log.logger.warning("Failed to receive/send data" + e);
        }
    }

    /**
     * Accept new client and bind the SocketChannel to selector
     * @param server Used to build new SocketChannel for Client
     * @param selector Bind the SocketChannel with selector
     */
    private void newClient(ServerSocketChannel server, Selector selector) {
        try {
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            Log.logger.info("New client from " + client.getRemoteAddress());
            client.register(selector, OP_READ, client);
        } catch (IOException e) {
            Log.logger.warning("Failed to accept client" + e);
        }
    }

    /**
     * unregister the SocketChannel
     * @param s The selection key which need to unbind
     */
    private void unregister(SelectionKey s) {
        try {
            s.cancel();
            s.channel().close();
        } catch (IOException e) {
            Log.logger.warning("Failed to clean up" + e);
        }
    }

    /**
     * Listener is the function of keep listening the port and pass the request
     * @param port port number which will be listened
     * @author Tiancheng
     */
    private void Listener(int port) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, OP_ACCEPT, null);

            while (true) {
                selector.select();
                for (SelectionKey sk : selector.selectedKeys()) {
                    // Acceptable means there is a new incoming
                    if (sk.isAcceptable()) {
                        newClient(serverSocketChannel, selector);

                        // Readable means this client has sent data or closed
                    } else if (sk.isReadable()) {
                        readAndEcho(sk);
                    }
                }
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            System.out.println("HTTPServer: " + e.getMessage());
        }
    }
}