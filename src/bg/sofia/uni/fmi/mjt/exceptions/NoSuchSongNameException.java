package bg.sofia.uni.fmi.mjt.exceptions;

public class NoSuchSongNameException extends Exception {

    public NoSuchSongNameException(String message) {
        super(message);
    }

    public NoSuchSongNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
