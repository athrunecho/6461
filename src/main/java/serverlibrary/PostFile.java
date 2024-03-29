package serverlibrary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostFile {

    public void PostFile(ResponseFrame response, String fileAddress, String content, Boolean overwrite) {

        try {
            //String messageBody = "";

            File file = new File(fileAddress);

            if (!file.exists()) {
                //create file
                if (file.createNewFile()) {
                    Log.logger.info("create file " + file.getName() + " success");
                    FileWriter fileWriter = new FileWriter(file, false);
                    fileWriter.write(content);
                    fileWriter.flush();
                    fileWriter.close();
                    response.Set200();
                    return;
                } else {
                    Log.logger.info("failed to create file " + file.getName());
                    response.Set404();
                    return;
                }
            }

            if (file.isFile()) {
                //Detect whether the file support write in
                if (!file.canWrite()) {
                    response.Set403();
                    Log.logger.warning(file.getName() + " can not write in");
                    return;
                }
                //Overwrite
                if (overwrite) {
                    FileWriter fileWriter = new FileWriter(file, false);
                    fileWriter.write(content);
                    fileWriter.flush();
                    fileWriter.close();
                    response.Set200();
                    Log.logger.info("overwrite to file " + file.getName() + " success");
                    return;
                }
                //append to the end of the file
                FileWriter fileWriter = new FileWriter(file, true);
                content = content + System.getProperty("line.separator");
                fileWriter.write(content);
                fileWriter.flush();
                fileWriter.close();
                response.Set200();
                Log.logger.info("append to file " + file.getName() + " success");
                return;
            }
            response.Set404();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
