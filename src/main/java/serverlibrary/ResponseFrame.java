package serverlibrary;

import java.util.Date;
import java.text.SimpleDateFormat;

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
     *
     */
    public void Set200(){
        this.Status = "200 OK\r\n";
    }

    /**
     *
     */
    public void Set404(){
        this.Status = "404 Not Found\r\n";
    }

    /**
     *
     */
    public void Set403(){
        this.Status = "403 Forbidden\r\n";
    }

    /**
     *
     */
    public void SetMessage(String message){
        this.Body += message;
        this.ContentLength = "Content-Length:" + String.valueOf(this.Body.length() + "\r\n");
    }

    /**
     *
     */
    public void SetContentType(String type){
       this.ContentType = "Content-Type:" + type;
    }

    /**
     *
     */
    public void SetDisposition(){

    }

    /**
     *
     * @return
     */
    public String toString(){

        // get time stamp
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        this.Date = "Date:" + df.format(new Date()) + "\r\n";

        String Message = this.HTTPVersion + this.Status;

        Message += this.Date;

        if(!this.ContentLength.isEmpty()){
            Message += this.ContentLength;
        }

        if(!this.ContentType.isEmpty()){
            Message += this.ContentType;
        }

        if(!this.ContentDisposition.isEmpty()){
            Message += this.ContentDisposition;
        }

        Message += this.Connection + "\r\n";

        Message += this.Body;

        return Message;
    }

    public static String mapping(String name) {
        name = name.toLowerCase().trim();
        System.out.println("mapping: "+ name);

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
}
