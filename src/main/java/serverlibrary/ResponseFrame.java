package serverlibrary;

public class ResponseFrame {

    private String HTTPVersion = "HTTP/1.0 ";
    private String Status = "";
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
    public void SetDisposition(){

    }

    /**
     *
     * @return
     */
    public String toString(){

        String Message = this.HTTPVersion + this.Status;
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

}
