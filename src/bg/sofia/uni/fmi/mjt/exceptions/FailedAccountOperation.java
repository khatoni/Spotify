package bg.sofia.uni.fmi.mjt.exceptions;

public class FailedAccountOperation extends Exception {

    public FailedAccountOperation(String message) {
        super(message);
    }

    public FailedAccountOperation(String message, Throwable cause) {
        super(message, cause);
    }
}
