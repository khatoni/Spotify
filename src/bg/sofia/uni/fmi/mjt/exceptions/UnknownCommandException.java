package bg.sofia.uni.fmi.mjt.exceptions;

public class UnknownCommandException extends Exception {

    public UnknownCommandException(String message) {
        super(message);
    }

    public UnknownCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
