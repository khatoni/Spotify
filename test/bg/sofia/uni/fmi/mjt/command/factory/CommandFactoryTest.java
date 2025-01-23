package bg.sofia.uni.fmi.mjt.command.factory;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.AddSongCommand;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.command.factory.CommandFactory;
import bg.sofia.uni.fmi.mjt.command.commands.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.command.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.command.commands.PLaySongCommand;
import bg.sofia.uni.fmi.mjt.command.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.command.commands.SearchCommand;
import bg.sofia.uni.fmi.mjt.command.commands.ShowPlaylistCommand;
import bg.sofia.uni.fmi.mjt.command.commands.TopSongsCommand;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class CommandFactoryTest {
    private static final User user = mock();

    @Test
    void testCreateUnknownCommand() {
        String command = "add:song:to playlist1";
        assertThrows(UnknownCommandException.class, () -> CommandFactory.createCommandFromString(user, command),
            "Unknown command was expected, but was not thrown");
    }

    @Test
    void testCreateAddSongCommand() throws UnknownCommandException, InvalidArgumentsCommandException {
        String commandLine = "add-song-to playlist1 song1";
        Command command = CommandFactory.createCommandFromString(user, commandLine);
        assertTrue(command instanceof AddSongCommand,
            "The created command was expected to be instance of AddSongCommand");
    }

    @Test
    void testCreatePlaylistCommand() throws UnknownCommandException, InvalidArgumentsCommandException {
        String commandLine = "create-playlist playlist2";
        Command command = CommandFactory.createCommandFromString(user, commandLine);
        assertTrue(command instanceof CreatePlaylistCommand,
            "The created command was expected to be instance of CreatePlaylist command");
    }

    @Test
    void testCreateLoginCommand() throws UnknownCommandException, InvalidArgumentsCommandException {
        String commandLine = "login antonio antonio";
        Command command = CommandFactory.createCommandFromString(user, commandLine);
        assertTrue(command instanceof LoginCommand,
            "The created command was expected to be instance of LoginCommand command");
    }

    @Test
    void testCreateRegisterCommand() throws UnknownCommandException, InvalidArgumentsCommandException {
        String commandLine = "register antonio antonio";
        Command command = CommandFactory.createCommandFromString(user, commandLine);
        assertTrue(command instanceof RegisterCommand,
            "The created command was expected to be instance of RegisterCommand command");
    }

    @Test
    void testCreatePlaySongCommand() throws UnknownCommandException, InvalidArgumentsCommandException {
        String commandLine = "play song2";
        Command command = CommandFactory.createCommandFromString(user, commandLine);
        assertTrue(command instanceof PLaySongCommand,
            "The created command was expected to be instance of PlaySong command");
    }

    @Test
    void testCreateSearchCommand() throws UnknownCommandException, InvalidArgumentsCommandException {
        String commandLine = "search word1 word2 word3";
        Command command = CommandFactory.createCommandFromString(user, commandLine);
        assertTrue(command instanceof SearchCommand,
            "The created command was expected to be instance of SearchCommand command");
    }

    @Test
    void testCreateShowPlaylistCommand() throws UnknownCommandException, InvalidArgumentsCommandException {
        String commandLine = "show-playlist playlist1";
        Command command = CommandFactory.createCommandFromString(user, commandLine);
        assertTrue(command instanceof ShowPlaylistCommand,
            "The created command was expected to be instance of ShowPlaylist command");
    }

    @Test
    void testCreateTopSongsCommand() throws UnknownCommandException, InvalidArgumentsCommandException {
        String commandLine = "top 10";
        Command command = CommandFactory.createCommandFromString(user, commandLine);
        assertTrue(command instanceof TopSongsCommand,
            "The created command was expected to be instance of TopSong command");
    }

    @Test
    void testInvalidArgumentsCommand() {
        String commandLine = "top 14.5";
        assertThrows(InvalidArgumentsCommandException.class,
            () -> CommandFactory.createCommandFromString(user, commandLine),
            "InvalidArgumentsCommandException was expected, but was not thrown");
    }

}

