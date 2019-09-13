package httpclient;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class Httpc {

    private static void runClient(SocketAddress endpoint) throws IOException {
        try (SocketChannel socket = SocketChannel.open()) {
            socket.connect(endpoint);


        }
    }
}
