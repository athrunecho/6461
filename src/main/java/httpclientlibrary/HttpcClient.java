package httpclientlibrary;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import static java.util.Arrays.asList;

/**
 *
 */
public class HttpcClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpcClient.class);

    /**
     *
     * @param address
     */
    private static void runClient(SocketAddress address) {
        try{
            SocketChannel channel = SocketChannel.open();
            channel.connect(address);

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] cmds = line.split(" ");

                if(line.equals("close")){
                    channel.close();
                    return;
                }else if(cmds[0].equals("httpc") && cmds.length > 1) {
                    if(cmds[1].equals("help")){

                        if(cmds.length > 2){
                            // Check HELP with argument
                            HTTPHelp.help(cmds[2]);
                        }else{
                            //Without argument
                            HTTPHelp.help();
                        }
                    }else{
                        // GET or POST request
                        requestHandler(channel, line);
                    }
                }else{
                    System.out.println("Unexpected command");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param channel
     * @param line
     */
    private static void requestHandler(SocketChannel channel, String line) {
        String[] cmds = line.split(" ");
        if(cmds[0].equals("httpc") && cmds.length > 1){

            switch(cmds[1]){
                case "get":
                    HTTPGet.get(channel, cmds);
                    break;
                case "post":
                    HTTPPost.post(channel, cmds);
                    break;
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

        // Send socket address
        runClient(new InetSocketAddress(host, port));
    }
}