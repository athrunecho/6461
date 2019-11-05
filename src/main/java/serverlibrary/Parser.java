package serverlibrary;

import static serverlibrary.HTTPServer.directory;

public class Parser {

    public synchronized static String Parse(String content) {

        Log.logger.info("request: \n" + content);
        String[] complete = content.split("\r\n\r\n");

        //System.out.println("Complete-Length: " + complete.length);
        //System.out.println("Complete0: " + complete[0]);



        String[] headers = complete[0].split("\r\n");
        //String[] bodies = entire[1].split("\r\n");

        String firstLine = headers[0];
        String[] slice = firstLine.split(" ");
        ResponseFrame response = new ResponseFrame();

        try {
            String filePath = slice[1];
            String entirePath = directory + filePath;

            String type = "";
            String disposition = "inline";
            Boolean overWrite = true;

            // Header checker
            for (int i = 0; i < headers.length; i++) {

                if (headers[i].contains("Accept")) {
                    type = headers[i].replaceAll("Accept:", "");
                }

                if (headers[i].contains("OverWrite")) {
                    overWrite = Boolean.parseBoolean(headers[i].replaceAll("OverWrite:", ""));
                }


                if (headers[i].contains("Content-Disposition")) {
                    disposition = headers[i].replaceAll(" Content-Disposition:", "");
                    ;
                }
            }

            if (firstLine.contains("GET")) {
                GetFile gf = new GetFile();
                gf.GetFile(response, entirePath, type, disposition);

            } else if (firstLine.contains("POST")) {
                PostFile pf = new PostFile();
                pf.PostFile(response, entirePath, complete[1], overWrite);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
