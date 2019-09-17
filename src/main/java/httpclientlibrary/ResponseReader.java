package httpclientlibrary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseReader {

    // readFully reads until the request is fulfilled or the socket is closed
    public static void readResponse(SocketChannel channel, String arg) throws IOException {

        // Need to be improved
        ByteBuffer buf = ByteBuffer.allocate(2048);
        StringBuffer stringBuf =new StringBuffer();

        channel.read(buf);
        buf.flip();

        while(buf.hasRemaining()){
            stringBuf.append((char)buf.get());
        }
        System.out.println(stringBuf.toString().trim());
    }
}
