package httpclientlibrary;

import java.util.HashMap;

/**
 *
 */
public class HTTPRequestModule {

    private
        String HTTPMethods;
        String HTTPVersion = "HTTP/1.0\r\n";
        String Host;
        String URL;
        String ConnectionStatus = "Connection: Keep-Alive\r\n";
        String ContentLength;
        HashMap<String, String> HeaderMap = new HashMap<>();
        String Body;

    /**
     *
     * @param method
     */
    public void setMethod(String method){
        this.HTTPMethods = method;
    }

    /**
     *
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
        this.ContentLength = String.valueOf(content.length());
        this.Body = content;
    }

    /**
     *
     * @return
     */
    public String printRequest(){
        return HTTPMethods + " " + URL + " " + HTTPVersion + ConnectionStatus +"\r\n";
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

        for(int i=2;i<args.length;i++){
            if(args[i].contains("http://")){
                request.URL = args[i].replaceAll("'","");
            }else{
                System.out.println("URL not found");
                return null;
            }
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
                    if(!filePath.contains("'")){
                        System.out.println("invalid inline data format");
                        return null;
                    }
                    request.setBody(filePath);
                }
            }

        }
        return request;
    }
}
