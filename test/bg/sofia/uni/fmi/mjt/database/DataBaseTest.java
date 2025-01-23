package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.exceptions.FailedDataBaseConnectionException;
import org.junit.jupiter.api.Test;

public class DataBaseTest {

    @Test
    void testSingleton() throws FailedDataBaseConnectionException {

       /* DataBase first = DataBase.getInstance();
        DataBase second = DataBase.getInstance();
        assertSame(first, second, "The two instances must be the same");
        */
    }
}
