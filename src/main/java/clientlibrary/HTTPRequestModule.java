package clientlibrary;

import java.util.HashMap;

/**
 * HTTPRequestModule is an abstract class
 */
public class HTTPRequestModule {

    public String HTTPVersion = "HTTP/1.0\r\n";
    public String Host;
    public String URL;
    public String ContentLength = "";
    public String ConnectionStatus = "Connection:Keep-Alive\r\n";
    public String Body = "";
    public HashMap<String, String> HeaderMap = new HashMap<>();

    /**
     * setServerInfo write the host and url of server address
     * @author Tiancheng
     * @param url server url
     */
    public void setServerInfo(String url){
        url = url.replaceAll("\'","");
        try {
            this.URL = url;
            java.net.URL URL = new java.net.URL(url);
            this.Host = URL.getHost();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * setBody put content into HTTP request body
     * @author Tiancheng
     * @param content HTTP request body
     */
    public void setBody(String content){
        this.Body += content;
        this.ContentLength = "Content-Length:" + String.valueOf(this.Body.length());
    }

    /**
     * Override method for self define
     * @author Tiancheng
     * @return empty
     */
    public String printRequest(){
        return "";
    }

}