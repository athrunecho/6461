package httpclientlibrary;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import static java.util.Arrays.asList;

public class HttpcClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpcClient.class);

    private static void runClient(Socket socket) throws IOException {
        try{
            System.out.println("Build Socket Success");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] cmds = line.split(" ");

                if(line.equals("close")){
                    return;
                }else if(cmds[0].equals("httpc") && cmds.length > 1) {
                    if(cmds[1].equals("help")){
                        if(cmds.length > 2){
                            HTTPHelp.help(cmds[2]);
                        }else{
                            HTTPHelp.help();
                        }
                    }else{
                        requestHandler(socket, line);
                    }
                }

                System.out.println("?????");
            }
        }finally {
            System.out.println("why?");
            socket.close();
        }
    }

    private static void requestHandler(Socket socket, String line) {
        String[] cmds = line.split(" ");
        if(cmds[0].equals("httpc") && cmds.length > 1){

            switch(cmds[1]){
                case "get":
                    HTTPGet.get(socket, cmds);
                case "post":
                    HTTPPost.post(socket);
                default:
                    logger.info("wtf");
            }
        }
        return;
    }

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("host", "h"), "EchoServer hostname")
                .withOptionalArg()
                .defaultsTo("httpbin.org");

        parser.acceptsAll(asList("port", "p"), "EchoServer listening port")
                .withOptionalArg()
                .defaultsTo("80");

        OptionSet opts = parser.parse(args);
        String host = (String) opts.valueOf("host");
        int port = Integer.parseInt((String) opts.valueOf("port"));
        Socket socket = new Socket(host, port);

        // Send socket address
        runClient(socket);
    }
}