package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.music.Song;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;

import java.util.Collection;
import java.util.List;

public class TopSongsCommand implements Command {

    private static final int NUMBER_OF_SONGS_INDEX = 0;
    private final int numberOfSongs;
    private final User attachedUser;

    public TopSongsCommand(User user, List<String> arguments) throws InvalidArgumentsCommandException {
        if (arguments.size() != 1) {
            throw new InvalidArgumentsCommandException(
                "Attempt to create TopSongsCommand with invalid number of arguments, expected 1");
        }

        try {
            numberOfSongs = Integer.parseInt(arguments.get(NUMBER_OF_SONGS_INDEX));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsCommandException(
                "Attempt to create TopSongsCommand with invalid parameter, expected int");
        }
        this.attachedUser = user;
    }

    @Override
    public String execute(DataBase dbConnection) {
        if (attachedUser == null) {
            return ServerResponse.NOT_AUTHENTICATED;
        }

        Collection<Song> trendingSongs = dbConnection.getMostTrendingSongs(numberOfSongs);
        return trendingSongs.toString() + System.lineSeparator();
    }
}
