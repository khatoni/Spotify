package bg.sofia.uni.fmi.mjt.access;

import bg.sofia.uni.fmi.mjt.exceptions.FailedRegistrationException;
import bg.sofia.uni.fmi.mjt.exceptions.UserAlreadyExistsException;

public interface Registrable {

    /**
     * @param user - the user to register
     * @return User - the saved user
     * @throws IllegalArgumentException    - if user is null
     * @throws UserAlreadyExistsException  - if the user is already registered
     * @throws FailedRegistrationException - if an internal problem occurs during the process
     */
    User registerAccount(User user)
        throws UserAlreadyExistsException, FailedRegistrationException;
}
