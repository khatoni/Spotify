package bg.sofia.uni.fmi.mjt.logger;

import bg.sofia.uni.fmi.mjt.access.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.SelectionKey;

public class Logger {

    private static Logger instance = null;
    private static final String LOG_FILE_NAME = "./log.txt";

    private final Writer fileWriter;

    private Logger(Writer writer) {
        this.fileWriter = writer;
    }

    public static Logger of() throws IOException {
        if (instance == null) {
            instance = new Logger(new FileWriter(LOG_FILE_NAME, true));
        }
        return instance;
    }

    public void log(String information, Exception exception, SelectionKey key) throws IOException {
        String user = retrieveUser(key);
        String fullInformation = user + "operation: " + information + exception.getClass() + exception.getMessage() +
            exception.getStackTrace().toString() + System.lineSeparator();
        fileWriter.write(fullInformation);
        fileWriter.flush();
    }

    public void log(String information, SelectionKey key) throws IOException {
        String user = retrieveUser(key);
        String fullInformation = user + "operation: " + information;
        fileWriter.write(fullInformation);
        fileWriter.flush();
    }

    public void log(String information, Exception e) throws IOException {
        fileWriter.write(information + e.getClass() + " " + e.getMessage() + " " + e.getStackTrace().toString());
        fileWriter.flush();
    }

    private String retrieveUser(SelectionKey key) {
        User triggeredUser = (User) key.attachment();
        String user;
        if (triggeredUser == null) {
            user = "Anonymous user ";
        } else {
            user = triggeredUser.getUsername();
        }
        return user;
    }
}
