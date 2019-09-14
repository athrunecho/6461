package httpclientlibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPGet {

    private static final Logger logger = LoggerFactory.getLogger(HTTPGet.class);

    public static void get(Socket socket, String URL) {
        try{
            String httpRequest = "";
            OutputStream request = socket.getOutputStream();
            request.write(httpRequest.getBytes());
            request.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
