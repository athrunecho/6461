package serverlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GetFile {

    public void GetFile(ResponseFrame response, String fileAddress, String type, String disposition) {

        String messageBody = "";

        if (fileAddress.contains("/../")) {
            response.Set403();
            return;
        }

        File file = new File(fileAddress);

        if (!file.exists()) {
            response.Set404();
            return;
        }

        File[] array = file.listFiles();
        String[] str = fileAddress.split("/");
        response.Set200();

        // get the folder list(with or without type check)
        if (fileAddress.endsWith("/")) {
            if (!type.isEmpty()) {
                // Has content type require
                for (int i = 0; i < array.length; i++) {
                    String[] types = type.split("/");
                    for (int j = 0; j < types.length; j++) {
                        if (array[i].getName().contains(types[j])) {
                            //Read file names and put into body
                            if (array[i].isFile()) {
                                messageBody += array[i].getName() + "\n";
                            }
                        }
                    }
                }
            } else {
                // No content type require
                for (int i = 0; i < array.length; i++) {
                    if (array[i].isFile()) {
                        messageBody += array[i].getName() + "\n";
                    } else if (array[i].isDirectory()) {
                        messageBody += array[i].getName() + "/\n";
                    }
                }
            }

            if (messageBody.isEmpty()) {
                messageBody = "empty folder";
            }
            response.SetMessage(messageBody);
            return;
        }

        // Have an specific filename
        if (str[str.length - 1].contains(".")) {
            if (file.isFile()) {

                if (!file.canRead()) {
                    response.Set403();
                    return;
                }

                if (file.getName().equals(str[str.length - 1])) {
                    //Read file and put into body
                    try {
                        BufferedReader in = new BufferedReader(new FileReader(file));
                        String buffer;
                        while ((buffer = in.readLine()) != null) {
                            messageBody += buffer;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    response.SetDisposition(disposition, str[str.length - 1]);
                    response.SetContentType(ResponseFrame.mapping(str[str.length - 1]));
                    response.SetMessage(messageBody);
                    return;
                }
            }
            response.Set404();
        }
    }
}