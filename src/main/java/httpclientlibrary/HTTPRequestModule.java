package httpclientlibrary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.io.File;
import java.util.Iterator;

/**
 *
 */
public class HTTPRequestModule {

    private String HTTPMethods;
    private String HTTPVersion = "HTTP/1.0\r\n";
    private String Host;
    private String URL;
    private String ConnectionStatus = "Connection:Keep-Alive\r\n";
    private String ContentLength = "Content-Length:";
    private HashMap<String, String> HeaderMap = new HashMap<>();
    private String Body;

    /**
     *
     * @param method
     */
    public void setMethod(String method){
        this.HTTPMethods = method;
    }

    /**
     * @author
     * @param key
     * @param value
     */
    public void addHeader(String key, String value){
        HeaderMap.put(key, value);
    }

    /**
     *
     * @param content
     */
    public void setBody(String content){
        this.ContentLength = ContentLength+String.valueOf(content.length());
        this.Body = content;
    }

    /**
     *
     * @return
     */
    public String printRequest(){

        if(!HeaderMap.isEmpty()){
            String diy = "";

            Iterator<HashMap.Entry<String, String>> entries = HeaderMap.entrySet().iterator();

            while (entries.hasNext()) {

                HashMap.Entry<String, String> entry = entries.next();

                diy += entry.getKey() + ":" + entry.getValue() + "\r\n";
            }

            return HTTPMethods + " " + URL + " " + HTTPVersion
                    + diy
                    + ContentLength + "\r\n"
                    + ConnectionStatus + "\r\n"
                    + Body+ "\r\n";
        }else {
            return HTTPMethods + " " + URL + " " + HTTPVersion
                    + ContentLength + "\r\n"
                    + ConnectionStatus + "\r\n"
                    + Body+ "\r\n";
        }
    }

    /**
     *
     * @param method
     * @param args
     * @return
     */
    public static HTTPRequestModule requestBuilder(String method, String[] args){

        HTTPRequestModule request = new HTTPRequestModule();

        // Set request method
        request.setMethod(method);

        for(int i=1;i<args.length;i++){
            if(args[i].contains("http://")){
                request.URL = args[i].replaceAll("'","");
                try {
                    java.net.URL url = new java.net.URL(request.URL);
                    request.Host = url.getHost();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        if(request.URL == null){
            System.out.println("URL not found");
            return null;
        }

        for(int i=2;i<args.length;i++){
            // check "-h" parameter
            if(args[i].equals("-h")){
                String pair = args[i+1];
                if(!pair.contains(":")){
                    System.out.println("invalid parameter format");
                    return null;
                }
                String[] param = pair.split(":", 2);
                request.addHeader(param[0], param[1]);
            }

            if(request.HTTPMethods.equals("POST")){
                // check "-d" parameter
                if(args[i].equals("-d")){
                    String data = args[i+1];
                    if(!data.contains("'")){
                        System.out.println("invalid inline data format");
                        return null;
                    }
                    request.setBody(data);

                }else if(args[i].equals("-f")){
                    // Either "-d" or "-f" can be used but not both.
                    String filePath = args[i+1];
                    String content = "";
                    try {
                        File file = new File("src/"+filePath);
                        if(file.exists()){
                            BufferedReader in = new BufferedReader(new FileReader("src/"+filePath));
                            String str;
                            while ((str = in.readLine()) != null) {
                                content += str;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    request.setBody(content);
                }
            }

        }
        return request;
    }
}
