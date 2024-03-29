package clientlibrary;

import java.util.HashMap;
import java.util.Iterator;

/**
 * GetRequest is a builder to generate get request
 */
public class GetRequest extends RequestModule {

    public String HTTPMethods = "GET";

    /**
     * accept arguements and build the get request
     *
     * @param args arguments
     * @return GetRequest
     * @author Tiancheng
     */
    public static GetRequest requestBuilder(String[] args) {

        GetRequest request = new GetRequest();

        // Find and server url as well as host name
        for (int i = 1; i < args.length; i++) {
            if (args[i].contains("/")) {
                request.setServerInfo(args[i]);
            }
        }

        if (request.URL == null) {
            System.out.println("URL not found");
            return null;
        }

        for (int i = 2; i < args.length; i++) {
            // check "-h" parameter
            if (args[i].equals("-h")) {
                String pair = args[i + 1].replaceAll("\'", "");
                pair = pair.replaceAll("'", "");
                if (!pair.contains(":")) {
                    System.out.println("invalid parameter format");
                    return null;
                }
                String[] param = pair.split(":", 2);
                request.HeaderMap.put(param[0], param[1]);
            }
        }

        return request;
    }

    /**
     * print the HTTP request
     *
     * @return formal HTTP request
     * @author Tiancheng
     */
    public String printRequest() {
        if (!HeaderMap.isEmpty()) {
            String settings = "";
            Iterator<HashMap.Entry<String, String>> entries = HeaderMap.entrySet().iterator();

            // append to settings
            while (entries.hasNext()) {
                HashMap.Entry<String, String> entry = entries.next();
                settings += entry.getKey() + ":" + entry.getValue() + "\r\n";
            }

            // HTTP header
            return HTTPMethods + " " + URL + " " + HTTPVersion
                    + Host
                    + settings
                    + ConnectionStatus
                    + "\r\n"
                    // HTTP body
                    + Body;

        } else {
            // HTTP header
            return HTTPMethods + " " + URL + " " + HTTPVersion
                    + Host
                    + ConnectionStatus
                    + "\r\n"
                    // HTTP body
                    + Body;
        }
    }
}
