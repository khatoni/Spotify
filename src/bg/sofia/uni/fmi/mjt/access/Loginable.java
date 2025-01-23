package bg.sofia.uni.fmi.mjt.access;

import bg.sofia.uni.fmi.mjt.exceptions.FailedLoginException;

public interface Loginable {
    /**
     * @param toAuthenticate - the user to authenticate
     * @return boolean - true if the authentication process is successful, otherwise false
     * @throws IllegalArgumentException - if toAuthenticate is null
     * @throws FailedLoginException     - if internal error occurs during authentication
     */
    boolean authenticate(User toAuthenticate) throws FailedLoginException;
}
