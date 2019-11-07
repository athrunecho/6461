package serverlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GetFile {

    public void GetFile(ResponseFrame response, String fileAddress, String type, String disposition) {

        String messageBody = "";

        File file = new File(fileAddress);

        File[] array = file.listFiles();
        String[] str = fileAddress.split("/");
        String fName = str[str.length - 1];
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
            Log.logger.info("search directory " + fileAddress);
            response.SetMessage(messageBody);
            return;
        }

        // Have an specific filename
        if (fName.endsWith(".txt") || fName.endsWith(".html") || fName.endsWith(".xml") || fName.endsWith(".json")) {
            if (file.isFile()) {

                if (!file.canRead()) {
                    response.Set403();
                    Log.logger.warning(file.getName() + " can not read");
                    return;
                }

                if (file.getName().equals(fName)) {
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
                    response.SetDisposition(disposition, fName);
                    response.SetContentType(ResponseFrame.mapping(fName));
                    response.SetMessage(messageBody);
                    return;
                }
            }
        }else{
            fileAddress = fileAddress.replaceAll(fName, "");
            File search = new File(fileAddress);
            File[] list = search.listFiles();

            for (int k = 0; k < list.length; k++) {
                if (list[k].isFile()) {
                    if(!type.isEmpty()){
                        String[] types = type.split("/");
                        for (int j = 0; j < types.length; j++) {
                            if (list[k].getName().contains(types[j]) && list[k].getName().contains(fName)) {
                                if (list[k].isFile()) {
                                    messageBody += list[k].getName() + "\n";
                                }
                            }
                        }
                    }else{
                        if (list[k].getName().contains(fName)) {
                            if (list[k].isFile()) {
                                messageBody += list[k].getName() + "\n";
                            }
                        }
                    }
                }
            }

            if(!messageBody.isEmpty()){
                response.SetMessage(messageBody);
                return;
            }
        }
        response.Set404();
    }
}