package serverlibrary;

import java.net.URL;

public class Parser {

    public static void parse(String content){
        String[] entire = content.split("\r\n\r\n");
        String[] headers = entire[0].split("\r\n");
        //String[] bodies = entire[1].split("\r\n");

        String firstLine = headers[0];

        String[] slice = firstLine.split(" ");
        try {
            URL url = new URL(slice[1]);
            Log.logger.info("request towards " + url.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void searchDirectory(){
        //do something
    }

}
