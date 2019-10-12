package clientlibrary;

/**
 * HTTP help guide
 */
public class Help {

    /**
     * help guide for commands
     * @author Tiancheng
     */
    public static void help() {

        System.out.print(
                "httpc is a curl-like application but supports HTTP protocol only.\r\nUsage:\r\n" +
                        "   httpc command [arguments]\r\n" +
                        "The commands are:\r\n" +
                        "   get executes a HTTP GET request and prints the response.\r\n" +
                        "   post executes a HTTP POST request and prints the response.\r\n" +
                        "   help prints this screen.\r\n" +
                        "Use \"httpc help [command]\" for more information about a command.\r\n");
    }

    public static void help(String arguments) {

        switch (arguments) {

            case "get":
                System.out.println("usage: httpc get [-v] [-h key:value] URL\r\n" +
                        "Get executes a HTTP GET request for a given URL." +
                        "   -v  Prints the detail of the response such as protocol, status, and headers.\r\n" +
                        "   -h key:value    Associates headers to HTTP Request with the format 'key:value'.\r\n"
                );
                break;

            case "post":
                System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\r\n" +
                        "Post executes a HTTP POST request for a given URL with inline data or from file.\r\n" +
                        "   -v Prints the detail of the response such as protocol, status, and headers.\r\n" +
                        "   -h key:value Associates headers to HTTP Request with the format 'key:value'.\r\n" +
                        "   -d string Associates an inline data to the body HTTP POST request.\r\n" +
                        "   -f file Associates the content of a file to the body HTTP POST request.\r\n" +
                        "Either [-d] or [-f] can be used but not both.\r\n"
                );
                break;

            default:
                System.out.println("Unexpected Arguments");
        }
    }
}
