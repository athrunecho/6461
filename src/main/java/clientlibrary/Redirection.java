package clientlibrary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Redirection detect signal of redirection
 */
public class Redirection {

    /**
     * The Redirector continue redirect if receive 302 status
     *
     * @param channel     SocketChannel
     * @param httpRequest previous request
     * @param args        arguments
     * @param <T>         generic class accept GetRequest and PostRequest
     * @return server response
     * @throws IOException
     * @author Tiancheng
     */
    public static <T extends RequestModule> String redirector(SocketChannel channel, T httpRequest, String[] args)
            throws IOException {

        ByteBuffer buf = ByteBuffer.allocate(2048);
        StringBuffer stringBuf = new StringBuffer();

        channel.read(buf);
        buf.flip();

        while (buf.hasRemaining()) {
            stringBuf.append((char) buf.get());
        }
        String completeResponse = stringBuf.toString().trim();
        ResponseReader.readResponse(completeResponse, args);
        String lines[] = completeResponse.split("\r\n");
        String control = lines[0];

        //redirection signal
        while (control.contains("HTTP/1.0 302 FOUND")) {

            String redirect = "";
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains("location: ") || lines[i].contains("Location: ")) {
                    lines[i] = lines[i].replaceAll("location: ", "");
                    lines[i] = lines[i].replaceAll("Location: ", "");
                    redirect = lines[i];
                }
            }

            String originHost = httpRequest.Host;
            String newURL = "http://";
            if (redirect.length() > 0) {
                newURL = newURL + originHost + redirect;
            } else {
                return "";
            }

            httpRequest.setServerInfo(newURL);
            String print = httpRequest.printRequest();
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            buffer.put(print.getBytes());
            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }

            // recursion loop for redirection
            completeResponse = Redirection.redirector(channel, httpRequest, args);
            String[] m = completeResponse.split("\r\n");
            for (int j = 0; j < m.length; j++) {
                if (m[j].contains("HTTP/1.0")) {
                    control = m[j];
                }
            }
            //System.out.println("control:++++++++" + control);
        }
        return completeResponse;
    }
}
