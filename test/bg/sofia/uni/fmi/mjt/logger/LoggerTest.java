package bg.sofia.uni.fmi.mjt.logger;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertSame;

public class LoggerTest {

    @Test
    void testSingleton() throws IOException {
        Logger first = Logger.of();
        Logger second = Logger.of();
        assertSame(first, second, "The two references must be the same");
    }
}
