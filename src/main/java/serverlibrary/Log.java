package serverlibrary;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    public static Logger logger = Logger.getLogger(HTTPServer.class.getName());

    public static void initial(){
        logger.setLevel(Level.ALL);
    }

    public static void hide(){
        logger.setLevel(Level.OFF);
    }
}