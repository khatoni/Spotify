package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchSongNameException;
import bg.sofia.uni.fmi.mjt.exceptions.SongAlreadyAddedIntoPlaylist;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;

import java.util.List;

public class AddSongCommand implements Command {

    private String playlistName;
    private String songName;
    private final User attachedUser;

    private static final int PLAYLIST_NAME_INDEX = 0;
    private static final int SONG_NAME_INDEX = 1;
    private static final int EXPECTED_NUMBER_ARGUMENTS = 2;

    public AddSongCommand(User user, List<String> arguments) throws InvalidArgumentsCommandException {
        if (arguments.size() < EXPECTED_NUMBER_ARGUMENTS) {
            throw new InvalidArgumentsCommandException(
                "Attempt to create AddSongCommand with invalid number of arguments, expected 2");
        }

        this.playlistName = arguments.get(PLAYLIST_NAME_INDEX);
        this.songName = arguments.get(SONG_NAME_INDEX);
        this.attachedUser = user;
    }

    @Override
    public String execute(DataBase dbConnection) {
        if (attachedUser == null) {
            return ServerResponse.NOT_AUTHENTICATED;
        }
        try {
            dbConnection.addSongToPlaylist(attachedUser, playlistName, songName);
            return "Successfully added the song " + songName + " into " + playlistName + System.lineSeparator();
        } catch (NoSuchPlaylistException e) {
            return ServerResponse.NO_SUCH_PLAYLIST;
        } catch (NoSuchSongNameException e) {
            return ServerResponse.NO_SUCH_SONG;
        } catch (SongAlreadyAddedIntoPlaylist e) {
            return ServerResponse.SONG_ALREADY_IN_PLAYLIST;
        }
    }

}
