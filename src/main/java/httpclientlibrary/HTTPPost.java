package httpclientlibrary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 */
public class HTTPPost {

    /**
     *
     * @param channel
     * @param args
     */
    public static void post(SocketChannel channel, String[] args) {
        try{
            HTTPRequestModule postRequest = HTTPRequestModule.requestBuilder("POST", args);
            if(postRequest == null){
                return;
            }
            String httpRequest = postRequest.printRequest();

            // Using ByteBuffer to write into socket channel
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            buffer.put(httpRequest.getBytes());
            buffer.flip();

            while(buffer.hasRemaining()) {
                channel.write(buffer);
            }

            // Receive all what we have sent
            Redirection.redirector(channel, httpRequest, args);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}