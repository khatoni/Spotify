package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.AddSongCommand;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchSongNameException;
import bg.sofia.uni.fmi.mjt.exceptions.SongAlreadyAddedIntoPlaylist;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddSongCommandTest {

    @Test
    void testExecuteCommandNoSession() throws InvalidArgumentsCommandException {
        Command command = new AddSongCommand(null, List.of("test1", "test2"));
        DataBase dataBase = mock();
        String response = command.execute(dataBase);
        assertEquals(ServerResponse.NOT_AUTHENTICATED, response,
            "The expected response is that user is not authenticated");
    }

    @Test
    void testCreateCommandWithInvalidArguments() {
        assertThrows(InvalidArgumentsCommandException.class, () -> new AddSongCommand(null, new ArrayList<>()),
            "InvalidArgumentsCommandException was expected, but was not thrown");
    }

    @Test
    void testExecuteNoSuchPlaylist()
        throws SongAlreadyAddedIntoPlaylist, NoSuchSongNameException, NoSuchPlaylistException,
        InvalidArgumentsCommandException {
        User user = mock();
        DataBase dataBase = mock();
        when(dataBase.addSongToPlaylist(user, "test", "test")).thenThrow(
            new NoSuchPlaylistException("Such playlist does not exist"));
        Command command = new AddSongCommand(user, List.of("test", "test"));
        assertEquals(ServerResponse.NO_SUCH_PLAYLIST, command.execute(dataBase),
            "The actual response is different the expected");
    }

    @Test
    void testExecuteNoSuchSong()
        throws InvalidArgumentsCommandException, SongAlreadyAddedIntoPlaylist, NoSuchSongNameException,
        NoSuchPlaylistException {
        User user = mock();
        DataBase dataBase = mock();
        when(dataBase.addSongToPlaylist(user, "test", "test")).thenThrow(
            new NoSuchSongNameException("Such song does not exist"));
        Command command = new AddSongCommand(user, List.of("test", "test"));
        assertEquals(ServerResponse.NO_SUCH_SONG, command.execute(dataBase),
            "The actual response is different the expected");
    }

    @Test
    void testExecuteSongIsAlreadyAdded()
        throws SongAlreadyAddedIntoPlaylist, NoSuchSongNameException, NoSuchPlaylistException,
        InvalidArgumentsCommandException {
        User user = mock();
        DataBase dataBase = mock();
        when(dataBase.addSongToPlaylist(user, "test", "test")).thenThrow(
            new SongAlreadyAddedIntoPlaylist("Song is already added into playlist"));
        Command command = new AddSongCommand(user, List.of("test", "test"));
        assertEquals(ServerResponse.SONG_ALREADY_IN_PLAYLIST, command.execute(dataBase),
            "The actual response is different the expected");
    }

    @Test
    void testExecuteSuccessfully()
        throws SongAlreadyAddedIntoPlaylist, NoSuchSongNameException, NoSuchPlaylistException,
        InvalidArgumentsCommandException {
        User user = mock();
        DataBase dataBase = mock();
        when(dataBase.addSongToPlaylist(user, "test", "test")).thenReturn(true);
        Command command = new AddSongCommand(user, List.of("test", "test"));
        assertEquals("Successfully added the song test into test" + System.lineSeparator(), command.execute(dataBase),
            "The actual response is different the expected");
    }
}
