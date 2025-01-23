package bg.sofia.uni.fmi.mjt.exceptions;

public class SuchPlaylistAlreadyExistsException extends Exception {

    public SuchPlaylistAlreadyExistsException(String message) {
        super(message);
    }

    public SuchPlaylistAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
