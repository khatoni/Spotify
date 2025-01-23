package bg.sofia.uni.fmi.mjt.exceptions;

public class FailedLoginException extends FailedAccountOperation {

    public FailedLoginException(String message) {
        super(message);
    }

    public FailedLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
