package bg.sofia.uni.fmi.mjt.access;

import bg.sofia.uni.fmi.mjt.exceptions.FailedAccountOperation;
import bg.sofia.uni.fmi.mjt.exceptions.FailedRegistrationException;
import bg.sofia.uni.fmi.mjt.exceptions.UserAlreadyExistsException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;

public class AccountRegistration extends AccountAccess implements Registrable {

    @Override
    public User registerAccount(User user)
        throws FailedRegistrationException, UserAlreadyExistsException {

        if (user == null) {
            throw new IllegalArgumentException("Trying to register account, but user is null");
        }

        try {
            if (doesUserExist(openAccountsFileForReading(), user).isPresent()) {
                throw new UserAlreadyExistsException("Username already exists");
            }
            registerAccount(user, openAccountsFileForWriting());

        } catch (FailedAccountOperation e) {
            throw new FailedRegistrationException("Internal error while registration", e);
        }

        return user;
    }

    private void registerAccount(User user, OutputStream outputStream)
        throws FailedRegistrationException {

        try (Writer writer = new OutputStreamWriter(outputStream)) {
            SaltHashContainer hashedPassword = hashPassword(user.password);
            String dataToStore = user.username + "," + hashedPassword.hashedPassword() + "," + hashedPassword.salt() +
                System.lineSeparator();
            writer.write(dataToStore);
            writer.flush();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new FailedRegistrationException("Internal problem while creating reservation", e);
        }
    }

}
