package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.music.Song;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;

import java.util.Collection;
import java.util.List;

public class SearchCommand implements Command {

    private List<String> keyWords;
    private final User attachedUser;

    public SearchCommand(User user, List<String> keyWords) throws InvalidArgumentsCommandException {
        if (keyWords.isEmpty()) {
            throw new InvalidArgumentsCommandException("Attempt to create search commands without keywords");
        }

        this.keyWords = keyWords;
        this.attachedUser = user;
    }

    @Override
    public String execute(DataBase dbConnection) {
        if (attachedUser == null) {
            return ServerResponse.NOT_AUTHENTICATED;
        }

        Collection<Song> songCollection =
            dbConnection.searchSongsByKeywords(keyWords.toArray(new String[keyWords.size()]));
        return songCollection.toString() + System.lineSeparator();
    }
}
