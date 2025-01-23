package bg.sofia.uni.fmi.mjt.server;

public class ServerResponse {

    public static final String SECOND_AUTHORIZATION =
        "You are already registered and logged in" + System.lineSeparator();
    public static final String SUCCESSFUL_REGISTRATION = "Successful registration" + System.lineSeparator();
    public static final String DUPLICATE_USER = "User with the same email already exists" + System.lineSeparator();
    public static final String FAILED_OPERATION =
        "Internal server problem, could not proceed with the request" + System.lineSeparator();
    public static final String SUCCESSFUL_LOGIN = "Successful login" + System.lineSeparator();
    public static final String WRONG_AUTHENTICATION = "Wrong email or password" + System.lineSeparator();

    public static final String NOT_AUTHENTICATED =
        "You have no permission to access this functionality, login or register first" +
            System.lineSeparator();
    public static final String NO_SUCH_PLAYLIST = "There is no such playlist" + System.lineSeparator();

    public static final String NO_SUCH_SONG = "There is no such song in out database" + System.lineSeparator();
    public static final String SONG_ALREADY_IN_PLAYLIST =
        "There is no such song in out database" + System.lineSeparator();
    public static final String PLAYLIST_ALREADY_EXISTS =
        "You already have playlist with this name" + System.lineSeparator();
}
