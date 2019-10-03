package httpclientlibrary;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import static java.util.Arrays.asList;

/**
 *  An httpc client based on HTTP protocol
 */
public class HttpcClient {

    /**
     * start the client
     * @author Tiancheng
     * @param address HTTP address
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
     * switch different request
     * @author Tiancheng
     * @param channel socket channel of server
     * @param line argument string
     */
    private static void requestHandler(SocketChannel channel, String line) {

        String[] cmds = line.split(" ");
        try {
            if (cmds[0].equals("httpc") && cmds.length > 1) {

                switch (cmds[1]) {
                    case "get":
                        GetRequest getRequest = GetRequest.requestBuilder(cmds);
                        String getData = getRequest.printRequest();
                        HTTPSender.send(channel, getData);
                        // Receive all what we have sent
                        Redirection.redirector(channel, getRequest, cmds);
                        break;
                    case "post":
                        PostRequest postRequest = PostRequest.requestBuilder(cmds);
                        String postData = postRequest.printRequest();
                        HTTPSender.send(channel, postData);
                        Redirection.redirector(channel, postRequest, cmds);
                        break;
                    default:
                        System.out.println("only accept get, post and help request");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return;
    }

    /**
     * @author Tiancheng
     * @param args splited arguments
     */
    public static void main(String[] args) {
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