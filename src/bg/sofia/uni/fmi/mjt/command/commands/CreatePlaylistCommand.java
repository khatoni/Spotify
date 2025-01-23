package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;

import java.util.List;

public class CreatePlaylistCommand implements Command {

    private static final int PLAYLIST_NAME_INDEX = 0;

    private final String playlistName;
    private final User attachedUser;

    public CreatePlaylistCommand(User user, List<String> arguments) throws InvalidArgumentsCommandException {
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
            dbConnection.createPlaylist(attachedUser, playlistName);
            return "You have successfully created playlist " + playlistName + System.lineSeparator();
        } catch (SuchPlaylistAlreadyExistsException e) {
            return ServerResponse.PLAYLIST_ALREADY_EXISTS;
        }
    }
}
