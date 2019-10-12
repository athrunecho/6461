package clientlibrary;

import java.io.File;
import java.io.FileOutputStream;

/**
 * ResponseReader class gives command responses and print results
 */
public class ResponseReader {

    /**
     * ReadResponse method reads the responses from the server and prepares output format or command
     *
     * @param content
     * @param args
     * @author Anqi Wang
     */
    public static void readResponse(String content, String args[]) {
        String print;
        String HTTPResponse = content.trim();
        String[] split = HTTPResponse.split("\r\n\r\n");
        if (split.length > 1) {
            print = split[1];
        } else {
            print = split[0];
        }

        // For loop the arguments and do corresponding function.
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-v")) {
                print = HTTPResponse;
            }
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                byte[] sourceByte = print.getBytes();
                try {
                    // If the file not exist, create it at the root
                    File file = new File("./" + args[i + 1]);

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
        System.out.println(print + "\n");
    }
}
