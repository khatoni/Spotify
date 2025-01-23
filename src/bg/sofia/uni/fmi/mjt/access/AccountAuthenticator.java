package bg.sofia.uni.fmi.mjt.access;

import bg.sofia.uni.fmi.mjt.exceptions.FailedAccountOperation;
import bg.sofia.uni.fmi.mjt.exceptions.FailedLoginException;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public final class AccountAuthenticator extends AccountAccess implements Loginable {

    @Override
    public boolean authenticate(User userToAuthenticate) throws FailedLoginException {
        if (userToAuthenticate == null) {
            throw new IllegalArgumentException("The user to authenticate was null");
        }

        try {
            Optional<StoredUser> storedUser = doesUserExist(openAccountsFileForReading(), userToAuthenticate);
            return checkPasswordHashes(storedUser, userToAuthenticate);
        } catch (NoSuchAlgorithmException | FailedAccountOperation e) {
            throw new FailedLoginException("Internal problem while trying to authenticate user", e);
        }
    }

    private boolean checkPasswordHashes(Optional<StoredUser> storedUser, User userToAuthenticate) throws
        NoSuchAlgorithmException {

        if (storedUser.isPresent()) {
            String storedUserPassword = storedUser.get().password;
            String hashedPassword = hashPassword(userToAuthenticate.password, storedUser.get().salt);
            return storedUserPassword.equals(hashedPassword);
        }
        return false;
    }

}
