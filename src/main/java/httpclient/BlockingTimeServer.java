package httpclient;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;

public class BlockingTimeServer {

    //private static Logger logger = LoggerFactory.getLogger(BlockingEchoServer.class);

    private static void reportTime(SocketChannel socket) {
        // 2208988800L is the number of seconds elapsed from 1900 to 1970
        long now = Instant.now().getEpochSecond() + 2208988800L;
        ByteBuffer bs = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        bs.putInt((int) now);
        try (SocketChannel client = socket) {
            client.write(bs);
        } catch (IOException e) {
            System.out.println("Failed to report time to client" + e);
            //logger.error("Failed to report time to client", e);
        }
    }

    private static void listenAndServe(int port) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(port));
            //logger.info("Time server is listening at {}", server.getLocalAddress());
            System.out.println("Time server is listening at {}" + server.getLocalAddress());
            while (true) {
                SocketChannel client = server.accept();
                System.out.println("New client from {}" + client.getRemoteAddress());
                executor.submit(() -> reportTime(client));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("port", "p"), "Listening port")
                .withOptionalArg()
                .defaultsTo("8037");
        OptionSet opts = parser.parse(args);
        int port = Integer.parseInt((String) opts.valueOf("port"));
        listenAndServe(port);
    }
}
