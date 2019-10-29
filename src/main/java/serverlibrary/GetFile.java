package serverlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GetFile {

    public void GetFile(ResponseFrame response, String fileAddress, String type) {

        String messageBody = "";
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
            System.out.println("KKKKKK");
            if (file.isFile()) {
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

                    response.SetContentType(ResponseFrame.mapping(str[str.length - 1]));
                    response.SetMessage(messageBody);
                    return;
                }
            }
            response.Set404();
        } /*else {
            System.out.println("LLLLL");
            //Has name and content type requirement
            if (!type.isEmpty()) {
                System.out.println("DDDDD");
                for (int i = 0; i < array.length; i++) {
                    String[] types = type.split("/");
                    for (int j = 0; j < types.length; j++) {
                        if (array[i].getName().contains(types[j])) {
                            if (array[i].isFile()) {
                                if (array[i].getName().contains(str[str.length - 1])) {
                                    messageBody += array[i].getName() + "\n";
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("HHHHHHHHHHHHHHHHHHHHH");
                // No content type require but has part of name
                for (int i = 0; i < array.length; i++) {
                    if (array[i].isFile()) {
                        if (array[i].getName().contains(str[str.length - 1])){
                            System.out.println("HHHHHHHHHHHHHHHHHHHHH");
                            messageBody += array[i].getName() + "\n";
                        }
                    } else if (array[i].isDirectory()) {
                        if (array[i].getName().contains(str[str.length - 1])){
                            messageBody += array[i].getName() + "/\n";
                        }
                    }
                }
            }
            System.out.println("gg"+messageBody);
            if (messageBody.isEmpty()) {
                response.Set404();
                return;
            }
            response.SetMessage(messageBody);
        }
        */
    }
}