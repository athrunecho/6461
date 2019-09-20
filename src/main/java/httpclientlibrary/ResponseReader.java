package httpclientlibrary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseReader {

    // readFully reads until the request is fulfilled or the socket is closed
    public static void readResponse(SocketChannel channel, String args[]) throws IOException {

        // Need to be improved
        ByteBuffer buf = ByteBuffer.allocate(2048);
        StringBuffer stringBuf =new StringBuffer();

        channel.read(buf);
        buf.flip();

        while(buf.hasRemaining()){
            stringBuf.append((char)buf.get());
        }

        String completeResponse = stringBuf.toString().trim();
        String[] split = completeResponse.split("\r\n\r\n");
        String print = split[1];

        for(int i=0;i<args.length;i++){

            if(args[i].equals("-v")){
                print = completeResponse;
            }if(args[i].equals("-o")){
                byte[] sourceByte = split[1].getBytes();
                if(null != sourceByte) {
                    try {
                        File file = new File("./"+args[i+1]);        //文件路径（路径+文件名）
                        if (!file.exists()) {    //文件不存在则创建文件，先创建目录
                            File dir = new File(file.getParent());
                            dir.mkdirs();
                            file.createNewFile();
                        }
                        FileOutputStream outStream = new FileOutputStream(file);    //文件输出流用于将数据写入文件
                        outStream.write(sourceByte);
                        outStream.close();    //关闭文件输出流
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
        System.out.println(print);
    }
}
