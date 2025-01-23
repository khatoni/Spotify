package bg.sofia.uni.fmi.mjt.exceptions;

public class FailedDataBaseConnectionException extends Exception {

    public FailedDataBaseConnectionException(String message) {
        super(message);
    }

    public FailedDataBaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
