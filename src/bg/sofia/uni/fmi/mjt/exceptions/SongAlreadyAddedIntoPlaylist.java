package bg.sofia.uni.fmi.mjt.exceptions;

public class SongAlreadyAddedIntoPlaylist extends Exception {

    public SongAlreadyAddedIntoPlaylist(String message) {
        super(message);
    }

    public SongAlreadyAddedIntoPlaylist(String message, Throwable cause) {
        super(message, cause);
    }
}
