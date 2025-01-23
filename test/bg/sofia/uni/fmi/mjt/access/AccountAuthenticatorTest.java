package bg.sofia.uni.fmi.mjt.access;

import bg.sofia.uni.fmi.mjt.exceptions.FailedAccountOperation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountAuthenticatorTest {
    private static final String testFilePath =
        "C:\\Users\\Hp\\Desktop\\Modern-Java-Technologies-2023-2024\\Spotify\\test.txt";

    private static InputStream inputTestData;

    @BeforeAll
    static void prepareTestFile() throws IOException {
        File file = new File(testFilePath);

        try (FileWriter fileWriter = new FileWriter(file); PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println(
                "antonio,B0FFD5C20EBED0D4801DC575DABC06173F4F6F4CEBAAC6095F58A" +
                    "5FB02C039AAC84A4C50343DCAA4218621FD299BD6F3125F226535A060A" +
                    "56AB4EC6512E0D82A,79F9AA0F43CD4376");
            printWriter.println("toni@fmi.bg,CE98BAEECF6B93D7038C22427D2C5A45,88885F7FC50B63A5");

        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @AfterAll
    static void removeTestFile() {
        File file = new File(testFilePath);
        file.delete();
    }

    @Test
    void testAuthenticateUserIsNull() {
        AccountAuthenticator accountAuthenticator = new AccountAuthenticator();
        assertThrows(IllegalArgumentException.class, () -> accountAuthenticator.authenticate(null),
            "IllegalArgumentException was expected, but was not thrown");
    }

    @Test
    void testAuthenticateUserNoSuchUser()
        throws FileNotFoundException, FailedAccountOperation {
        AccountAccess access = mock();
        InputStream inputStream = new FileInputStream(testFilePath);
        when(access.openAccountsFileForReading()).thenReturn(inputStream);
        AccountAuthenticator accountAuthenticator = new AccountAuthenticator();
        assertFalse(accountAuthenticator.authenticate(new User("mimi", "mimi")));
    }

    @Test
    void testAuthenticateUserWrongPassword()
        throws FailedAccountOperation, FileNotFoundException {
        AccountAccess access = mock();
        InputStream inputStream = new FileInputStream(testFilePath);
        when(access.openAccountsFileForReading()).thenReturn(inputStream);
        AccountAuthenticator accountAuthenticator = new AccountAuthenticator();
        assertFalse(accountAuthenticator.authenticate(new User("antonio", "fmistude")));
    }

    @Test
    void testAuthenticateSuccessfully()
        throws FailedAccountOperation, FileNotFoundException {
        AccountAccess access = mock();
        InputStream inputStream = new FileInputStream(testFilePath);
        when(access.openAccountsFileForReading()).thenReturn(inputStream);
        AccountAuthenticator accountAuthenticator = new AccountAuthenticator();
        assertTrue(accountAuthenticator.authenticate(new User("toni@fmi.bg", "fmitoni")));
    }
}
