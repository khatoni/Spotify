package bg.sofia.uni.fmi.mjt.exceptions;

public class InvalidArgumentsCommandException extends Exception {

    public InvalidArgumentsCommandException(String message) {
        super(message);
    }

    public InvalidArgumentsCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
