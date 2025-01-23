package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.command.commands.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreatePlaylistTest {

    @Test
    void testExecuteCommandNoSession() throws InvalidArgumentsCommandException {
        Command command = new CreatePlaylistCommand(null, List.of("test1", "test2"));
        DataBase dataBase = mock();
        String response = command.execute(dataBase);
        assertEquals(ServerResponse.NOT_AUTHENTICATED, response,
            "The expected response is that user is not authenticated");
    }

    @Test
    void testCreateCommandWithInvalidArguments() {
        assertThrows(InvalidArgumentsCommandException.class, () -> new CreatePlaylistCommand(null, new ArrayList<>()),
            "InvalidArgumentsCommandException was expected, but was not thrown");
    }

    @Test
    void testExecuteSuccessfully() throws InvalidArgumentsCommandException, SuchPlaylistAlreadyExistsException {
        User user = mock();
        DataBase dataBase = mock();
        when(dataBase.createPlaylist(user, "test")).thenReturn(true);
        Command command = new CreatePlaylistCommand(user, List.of("test"));
        assertEquals("You have successfully created playlist test" + System.lineSeparator(),
            command.execute(dataBase),
            "The actual response is different the expected");
    }

    @Test
    void testExecutePlaylistAlreadyExists() throws SuchPlaylistAlreadyExistsException,
        InvalidArgumentsCommandException {
        User user = mock();
        DataBase dataBase = mock();
        when(dataBase.createPlaylist(user, "test")).thenThrow(
            new SuchPlaylistAlreadyExistsException("playlsit already exists"));
        Command command = new CreatePlaylistCommand(user, List.of("test"));
        assertEquals(ServerResponse.PLAYLIST_ALREADY_EXISTS,
            command.execute(dataBase),
            "The actual response is different the expected");
    }
}
