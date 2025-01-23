package bg.sofia.uni.fmi.mjt.command.factory;

public enum CommandType {
    REGISTER("register"),
    LOGIN("login"),
    SEARCH("search"),
    TOP("top"),
    CREATE_PLAYLIST("create-playlist"),
    ADD_SONG("add-song-to"),
    SHOW_PLAYLIST("show-playlist"),
    PLAY("play");

    private final String textRepresentation;

    CommandType(String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    public String toString() {
        return textRepresentation;
    }

    public static CommandType valueOfRepresentation(String label) {
        for (CommandType commandType : values()) {
            if (commandType.textRepresentation.equals(label)) {
                return commandType;
            }
        }
        return null;
    }
}
