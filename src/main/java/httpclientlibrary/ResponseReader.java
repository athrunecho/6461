package httpclientlibrary;

import java.io.File;
import java.io.FileOutputStream;

public class ResponseReader {

    // readResponse reads the response from the server and prepares output format
    public static void readResponse(String content, String args[]){

        String HTTPResponse = content.trim();
        String[] split = HTTPResponse.split("\r\n\r\n");
        String print = split[1];

        // For loop the arguments and do corresponding function.
        for(int i=0;i<args.length;i++){

            if(args[i].equals("-v")){
                print = HTTPResponse;
            }if(args[i].equals("-o")){
                byte[] sourceByte = split[1].getBytes();
                try {
                    // If the file not exist, create it at the root
                    File file = new File("./"+args[i+1]);
                    /*
                    if (!file.exists()) {
                        File dir = new File(file.getParent());
                        dir.mkdirs();
                        file.createNewFile();
                    }
                    */
                    // FileOutputStream to write in file
                    FileOutputStream outStream = new FileOutputStream(file);
                    outStream.write(sourceByte);
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        System.out.println(print);
    }
}
