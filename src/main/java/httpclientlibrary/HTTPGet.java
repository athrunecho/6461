package httpclientlibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class HTTPGet {

    private static final Logger logger = LoggerFactory.getLogger(HTTPGet.class);

    public static void get(SocketChannel channel, String[] args) {
        try{
            HTTPRequestModule getRequest = new HTTPRequestModule();
            String URL = "";
            for(int i=2;i<args.length;i++){
                if(args[i].contains("http://")){
                    URL = args[i].replaceAll("'","");
                }
            }
            getRequest.setMethodAndURL("GET", URL);
            String httpRequest = getRequest.printRequest();

            // Using ByteBuffer to write into socket channel
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            buffer.put(httpRequest.getBytes());
            buffer.flip();

            while(buffer.hasRemaining()) {
                channel.write(buffer);
            }

            String arg = "";
            // Receive all what we have sent
            ResponseReader.readResponse(channel, arg);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
