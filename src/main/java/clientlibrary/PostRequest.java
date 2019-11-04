package clientlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * PostRequest class analyses post request and print corresponding results
 */
public class PostRequest extends RequestModule {

    public String HTTPMethods = "POST";

    /**
     * PostRequest method receives post request, and analyse corresponding command.
     *
     * @param args
     * @return request
     * @author Anqi Wang
     */
    public static PostRequest requestBuilder(String[] args) {

        PostRequest request = new PostRequest();

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
                ;
                if (!pair.contains(":")) {
                    System.out.println("invalid parameter format");
                    return null;
                }
                String[] param = pair.split(":", 2);
                request.HeaderMap.put(param[0], param[1]);
            }

            // check "-d" parameter
            if (args[i].equals("-d")) {
                String inline = args[i + 1].replaceAll("\'", "");
                ;
                request.setBody(inline);

                // Either "-d" or "-f" can be used but not both
            } else if (args[i].equals("-f")) {
                String filePath = args[i + 1].replaceAll("\'", "");
                ;
                String content = "";
                try {
                    File file = new File("src/" + filePath);
                    if (file.exists()) {
                        BufferedReader in = new BufferedReader(new FileReader("src/" + filePath));
                        String str;
                        while ((str = in.readLine()) != null) {
                            content += str;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.setBody(content);
            }
        }

        return request;
    }

    /**
     * printRequest prints HTTP request in String type
     *
     * @return formal format of HTTP request
     * @author Anqi Wang
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

            if (ContentLength.length() > 0) {
                // Body is empty
                return HTTPMethods + " " + URL + " " + HTTPVersion
                        + Host
                        + settings
                        + ContentLength + "\r\n"
                        + ConnectionStatus
                        + "\r\n"
                        + Body;
            } else {
                return HTTPMethods + " " + URL + " " + HTTPVersion
                        + Host
                        + settings
                        + ConnectionStatus
                        + "\r\n"
                        + Body;
            }
        } else {
            if (ContentLength.length() > 0) {
                // Body is empty
                return HTTPMethods + " " + URL + " " + HTTPVersion
                        + Host
                        + ContentLength + "\r\n"
                        + ConnectionStatus
                        + "\r\n"
                        + Body;
            } else {
                return HTTPMethods + " " + URL + " " + HTTPVersion
                        + Host
                        + ConnectionStatus
                        + "\r\n"
                        + Body;
            }
        }
    }
}
