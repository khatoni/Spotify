package bg.sofia.uni.fmi.mjt.database;

import java.io.IOException;

public class DataBaseSnapShotter implements Runnable {

    private final DataBase dbConnection;

    public DataBaseSnapShotter(DataBase dataBase) {
        this.dbConnection = dataBase;
    }

    @Override
    public void run() {
        try {
            dbConnection.saveSnapshot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
