package httpclientlibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ResponseReader {

    // readFully reads until the request is fulfilled or the socket is closed
    public static void readResponse(Socket socket, String arg) {
        while (true) {
            try {
                String response;
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                while((response = in.readLine())!=null) {
                    System.out.println(response);
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
