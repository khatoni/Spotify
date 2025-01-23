package bg.sofia.uni.fmi.mjt.access;

import java.util.Objects;

public class User {

    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }
        if (!(other instanceof User that)) {
            return false;
        }

        return this.username.equals(that.username);
    }

    public int hashCode() {
        return Objects.hash(username);
    }

    public String getUsername() {
        return username;
    }
}
