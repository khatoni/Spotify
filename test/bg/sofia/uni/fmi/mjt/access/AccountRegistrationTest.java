package bg.sofia.uni.fmi.mjt.access;

import bg.sofia.uni.fmi.mjt.exceptions.FailedAccountOperation;
import bg.sofia.uni.fmi.mjt.exceptions.FailedRegistrationException;
import bg.sofia.uni.fmi.mjt.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountRegistrationTest {

    private static final String testFilePath =
        "C:\\Users\\Hp\\Desktop\\Modern-Java-Technologies-2023-2024\\Spotify\\test.txt";

    @BeforeAll
    static void prepareTestFile() throws IOException {
        File file = new File(testFilePath);

        try (FileWriter fileWriter = new FileWriter(file); PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println(
                "antonio,B0FFD5C20EBED0D4801DC575DABC06173F4F6F4CEBAAC6095F58A" +
                    "5FB02C039AAC84A4C50343DCAA4218621FD299BD6F3125F226535A060A" +
                    "56AB4EC6512E0D82A,79F9AA0F43CD4376");

        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @AfterAll
    static void clearTestFile() {
        File file = new File(testFilePath);
        file.delete();
    }

    @Test
    void testRegisterAccountWhenUserIsNull() {
        AccountRegistration accountRegistration = new AccountRegistration();
        assertThrows(IllegalArgumentException.class, () -> accountRegistration.registerAccount(null),
            "IllegalArgumentException was expected, but was not thrown");
    }

    @Test
    void testRegisterAccountWhenUserIsAlreadyRegistered() {
        AccountRegistration accountRegistration = new AccountRegistration();
        assertThrows(UserAlreadyExistsException.class,
            () -> accountRegistration.registerAccount(new User("antonio", "fmistar")),
            "UserAlreadyExistsException was expected, but was not thrown");
    }

    @Test
    void testRegisterAccountWhenUserIsNotRegistered()
        throws UserAlreadyExistsException, FailedAccountOperation, FileNotFoundException, NoSuchAlgorithmException {
        AccountRegistration accountRegistration = mock();
        InputStream inputStream = new FileInputStream(testFilePath);
        OutputStream outputStream = new FileOutputStream(testFilePath);
        when(accountRegistration.openAccountsFileForReading()).thenReturn(inputStream);
        when(accountRegistration.openAccountsFileForWriting()).thenReturn(outputStream);
        when(accountRegistration.registerAccount(any())).thenCallRealMethod();
        when(accountRegistration.hashPassword(any())).thenCallRealMethod();
        User expected = new User("vankata", "fmivnaka");
        User returned = accountRegistration.registerAccount(expected);
        assertEquals(expected, returned, "The stored user must be the same as the expected");
    }

    @Test
    void testRegisterAccountWithUserNotRegisteredHashException()
        throws FailedAccountOperation, NoSuchAlgorithmException, UserAlreadyExistsException {
        AccountRegistration accountRegistration = mock();
        when(accountRegistration.registerAccount(any())).thenCallRealMethod();
        when(accountRegistration.openAccountsFileForReading()).thenCallRealMethod();
        when(accountRegistration.openAccountsFileForWriting()).thenCallRealMethod();
        when(accountRegistration.hashPassword(any())).thenThrow(new NoSuchAlgorithmException());
        assertThrows(FailedRegistrationException.class,
            () -> accountRegistration.registerAccount(new User("mimi", "fmimimi")),
                "FailedRegistrationException was expected, but was not thrown");
    }

    @Test
    void testRegisterAccountWithUserIsNotRegisteredCaptureOutput()
        throws FailedAccountOperation, UserAlreadyExistsException, IOException,
        NoSuchAlgorithmException {
        AccountRegistration accountRegistration = mock();
        when(accountRegistration.registerAccount(any())).thenCallRealMethod();

        when(accountRegistration.hashPassword(any())).thenReturn(new SaltHashContainer("test1", "test2"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(accountRegistration.openAccountsFileForWriting()).thenReturn(outputStream);
        accountRegistration.registerAccount(new User("pepi", "pepifmi"));
        String captured = outputStream.toString();
        String expected = "pepi,test1,test2" + System.lineSeparator();
        assertEquals(captured, expected, "The stored data differs from the expected");
        outputStream.close();

    }

    @Test
    void testRegisterAccountFailedRegistration() throws IOException {
        AccountRegistration accountRegistration = new AccountRegistration() {
            @Override
            public OutputStream openAccountsFileForWriting() throws FailedAccountOperation {

                throw new FailedAccountOperation("Internal error");
            }
        };

        assertThrows(FailedRegistrationException.class,
            () -> accountRegistration.registerAccount(new User("hi", "hi")));

    }
}
