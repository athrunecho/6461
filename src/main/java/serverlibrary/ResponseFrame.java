package serverlibrary;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ResponseFrame is a factory for building HTTP response
 */
public class ResponseFrame {

    private String HTTPVersion = "HTTP/1.0 ";
    private String Status = "";
    private String Date = "";
    private String ContentLength = "";
    private String ContentType = "";
    private String ContentDisposition = "";
    private String Connection = "Connection:Keep-Alive\r\n";
    private String Body = "";

    /**
     * mapping is a method for get type of the file from its name
     * @param name
     * @return Content-Type:[type]
     */
    public static String mapping(String name) {
        name = name.toLowerCase().trim();

        if (name.endsWith(".txt")) {
            return "text/plain\r\n";
        } else if (name.endsWith(".html")) {
            return "text/html\r\n";
        } else if (name.endsWith(".xml")) {
            return "text/xml\r\n";
        } else if (name.endsWith(".json")) {
            return "application/json\r\n";
        }
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public static String reverseMapping(String name){
        name = name.toLowerCase().trim();

        if(name.contains("text/plain")){
            return ".txt";
        }else if(name.contains("text/html")){
            return ".html";
        }else if(name.contains("text/xml")){
            return ".xml";
        }else if(name.contains("application/json")){
            return ".json";
        }
        return null;
    }

    /**
     * Set200 indicate the response is fine as expected
     */
    public void Set200() {
        this.Status = "200 OK\r\n";
    }

    /**
     * Set404 indicate the server cannot find the file in the request
     */
    public void Set404() {
        this.Status = "404 Not Found\r\n";
    }

    /**
     * Set403 indicate that users are banned to attach the file
     */
    public void Set403() {
        this.Status = "403 Forbidden\r\n";
    }

    /**
     *
     */
    public void SetMessage(String message) {
        this.Body += message;
        this.ContentLength = "Content-Length:" + String.valueOf(this.Body.length() + "\r\n");
    }

    /**
     *
     */
    public void SetContentType(String type) {
        this.ContentType = "Content-Type:" + type;
    }

    /**
     *
     */
    public void SetDisposition(String param, String fileName) {

        param = param.replaceAll("Content-Disposition:", "");
        param = param.toLowerCase().trim();

        if (param.equals("inline")) {
            this.ContentDisposition = "Content-Disposition:inline\r\n";
            return;
        } else if (param.equals("attachment")) {
            this.ContentDisposition = "Content-Disposition:attachment; filename=\"" + fileName + "\"\r\n";
            return;
        }
    }

    /**
     * @return
     */
    public String toString() {
        // get time stamp
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        this.Date = "Date:" + df.format(new Date()) + "\r\n";

        String Message = this.HTTPVersion + this.Status;

        Message += this.Date;

        if (!this.ContentLength.isEmpty()) {
            Message += this.ContentLength;
        }

        if (!this.ContentType.isEmpty()) {
            Message += this.ContentType;
        }

        if (!this.ContentDisposition.isEmpty()) {
            Message += this.ContentDisposition;
        }

        Message += this.Connection + "\r\n";

        Message += this.Body;

        return Message;
    }
}