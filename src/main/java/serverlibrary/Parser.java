package serverlibrary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static serverlibrary.HTTPServer.directory;

public class Parser {

    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }

    /**
     * Parse is a method for parsing the HTTP request
     * @param content request content
     * @return prepared response
     */
    public synchronized static String Parse(String content) {

        Log.logger.info("request: \n" + content);
        String[] complete = content.split("\r\n\r\n");
        String[] headers = complete[0].split("\r\n");
        String firstLine = headers[0];
        String[] slice = firstLine.split(" ");
        ResponseFrame response = new ResponseFrame();
        String body = "";

        if(complete.length == 1){
            body = "";
        }else{
            body = complete[1];
        }

        try {
            String filePath = slice[1];
            String entirePath = directory + filePath;
            String type = "";
            String disposition = "inline";
            Boolean overWrite = true;
            String contentType = "";

            if(appearNumber(filePath, "/") > 1){
                response.Set403();
                return response.toString();
            }

            // Header checker
            for (int i = 0; i < headers.length; i++) {
                //
                if (headers[i].contains("Accept")) {
                    type = headers[i].replaceAll("Accept:", "");
                }
                //
                if (headers[i].contains("OverWrite")) {
                    overWrite = Boolean.parseBoolean(headers[i].replaceAll("OverWrite:", ""));
                }
                //
                if (headers[i].contains("Content-Disposition")) {
                    disposition = headers[i].replaceAll(" Content-Disposition:", "");
                }
                //
                if (headers[i].contains("Content-Type:") && firstLine.contains("POST") && !filePath.contains(".")) {
                    contentType = headers[i].replaceAll(" Content-Type:", "");
                    entirePath += ResponseFrame.reverseMapping(contentType);
                }
            }

            if (firstLine.contains("GET")) {
                GetFile gf = new GetFile();
                gf.GetFile(response, entirePath, type, disposition);

            } else if (firstLine.contains("POST")) {
                PostFile pf = new PostFile();
                pf.PostFile(response, entirePath, body, overWrite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.logger.info("response \n" + response.toString() + "\n");
        return response.toString();
    }
}
