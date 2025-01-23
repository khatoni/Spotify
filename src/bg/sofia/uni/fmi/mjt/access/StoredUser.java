package bg.sofia.uni.fmi.mjt.access;

public class StoredUser extends User {

    String salt;

    private StoredUser(String username, String password, String salt) {
        super(username, password);
        this.salt = salt;
    }

    public static StoredUser of(String line) {
        String[] tokens = line.split(",");
        String username = tokens[0];
        String password = tokens[1];
        String salt = tokens[2];
        return new StoredUser(username, password, salt);
    }
}
