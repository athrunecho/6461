package httpclientlibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPGet {

    private static final Logger logger = LoggerFactory.getLogger(HTTPGet.class);

    public static void get(Socket socket, String[] args) {
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

            System.out.println(httpRequest);

            OutputStream request = socket.getOutputStream();
            request.write(httpRequest.getBytes());
            request.flush();

            String arg = "";
            // Receive all what we have sent
            ResponseReader.readResponse(socket, arg);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
