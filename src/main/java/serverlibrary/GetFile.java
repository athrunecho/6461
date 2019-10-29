package serverlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GetFile {

    public void GetFile(ResponseFrame response, String fileAddress, String type){

        String[] str = fileAddress.split("/");

        String messageBody = "";

        File file = new File(fileAddress);

        //
        if(!file.exists()){
            response.Set404();
            return;
        }

        // get the folder list
        File[] array = file.listFiles();

        // Have an specific filename
        if(str[str.length-1].contains(".")){
            if(file.isFile()) {
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
                    response.SetMessage(messageBody);
                    response.Set200();
                    return;
                }
            }
            response.Set404();
            return;
        }else{
            // with content type require
            if(!type.isEmpty()){
                for(int i=0;i<array.length;i++) {
                    String[] types = type.split("/");
                    for (int j = 0; j < types.length; j++) {
                        if (array[i].getName().contains(types[j])) {
                            //Read file names and put into body
                            messageBody += array[i].getName() + "\n";
                        }
                    }
                }

            }else{
                // No content type require
                for(int i=0;i<array.length;i++){
                    if(array[i].isFile()){
                        messageBody += array[i].getName() + "\n";
                    /*
                    // only take file name   
                    System.out.println("^^^^^" + array[i].getName());
                    // take file path and name   
                    System.out.println("#####" + array[i]);
                    // take file path and name   
                    System.out.println("*****" + array[i].getPath());
                    */
                    }else if(array[i].isDirectory()){
                        messageBody += array[i].getName() + "/\n";
                    }
                }
            }
            //
            if(messageBody.isEmpty()){
                messageBody = "empty folder";
            }
            response.SetMessage(messageBody);
            response.Set200();
        }
    }
}