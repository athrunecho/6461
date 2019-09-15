package httpclientlibrary;

public class HTTPRequestModule {
    private
        String HTTPMethods;
        String HTTPVersion = "HTTP/1.0\r\n";
        String URL;
        String ConnectionStatus = "Connection: Keep-Alive\r\n";

    public void setMethodAndURL(String method, String url){
        this.HTTPMethods = method;
        this.URL = url;
    }

    public String printRequest(){
        return HTTPMethods + " " + URL + " " + HTTPVersion + ConnectionStatus +"\r\n";
    }
}
