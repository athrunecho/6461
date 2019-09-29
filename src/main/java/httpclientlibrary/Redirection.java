package httpclientlibrary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Redirection{

    public static String redirector(SocketChannel channel, String httpRequest, String[] args) throws IOException {

        ByteBuffer buf = ByteBuffer.allocate(2048);
        StringBuffer stringBuf = new StringBuffer();

        channel.read(buf);
        buf.flip();

        while (buf.hasRemaining()) {
            stringBuf.append((char) buf.get());
        }
        String completeResponse = stringBuf.toString().trim();
        ResponseReader.readResponse(completeResponse, args);

        /*
        if(completeResponse.contains("HTTP/1.0 302")){

            //redirection
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            buffer.put(httpRequest.getBytes());
            buffer.flip();

            while(buffer.hasRemaining()) {
                channel.write(buffer);
            }

            // recursion loop for redirection
            completeResponse = Redirection.redirector(channel, httpRequest, args);
        }
        */
        //
        return completeResponse;
    }
}
