package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.music.Song;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;

import java.util.Collection;
import java.util.List;

public class ShowPlaylistCommand implements Command {
    private static final int PLAYLIST_NAME_INDEX = 0;

    private final String playlistName;
    private final User attachedUser;

    public ShowPlaylistCommand(User user, List<String> arguments) throws InvalidArgumentsCommandException {
        if (arguments.isEmpty()) {
            throw new InvalidArgumentsCommandException(
                "Attempt to create CreatePlaylistCommand with invalid number of arguments, expected 1");
        }
        playlistName = arguments.get(PLAYLIST_NAME_INDEX);
        this.attachedUser = user;
    }

    @Override
    public String execute(DataBase dbConnection) {
        if (attachedUser == null) {
            return ServerResponse.NOT_AUTHENTICATED;
        }
        try {
            Collection<Song> songs = dbConnection.showPlaylistInfo(attachedUser, playlistName);
            return songs.toString() + System.lineSeparator();
        } catch (NoSuchPlaylistException e) {
            return ServerResponse.NO_SUCH_PLAYLIST;
        }
    }
}
