package bg.sofia.uni.fmi.mjt.exceptions;

public class FailedRegistrationException extends FailedAccountOperation {
    public FailedRegistrationException(String message) {
        super(message);
    }

    public FailedRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
