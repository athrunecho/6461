package serverlibrary;

import java.io.File;

public class PostFile {

    public void PostFile(ResponseFrame response, String fileAddress, String content, Boolean overwrite){

        String[] str = fileAddress.split("/");

        String messageBody = "";

        File file = new File(fileAddress);
        // get the folder list
        File[] array = file.listFiles();

        for(int i=0;i<array.length;i++){
            if(array[i].isFile()){
                if (array[i].getName().equals(str[str.length - 1])){
                    //

                }
            }
        }
    }
}
