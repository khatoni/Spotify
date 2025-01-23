package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.command.commands.SearchCommand;
import bg.sofia.uni.fmi.mjt.command.commands.ShowPlaylistCommand;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.music.Song;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchCommandTest {
    @Test
    void testExecuteCommandNoSession() throws InvalidArgumentsCommandException {
        Command command = new SearchCommand(null, List.of("test1"));
        DataBase dataBase = mock();
        String response = command.execute(dataBase);
        assertEquals(ServerResponse.NOT_AUTHENTICATED, response,
            "The expected response is that user is not authenticated");
    }

    @Test
    void testCreateCommandWithInvalidArguments() {
        assertThrows(InvalidArgumentsCommandException.class, () -> new SearchCommand(null, new ArrayList<>()),
            "InvalidArgumentsCommandException was expected, but was not thrown");
    }

    @Test
    void testExecuteSuccessfully() throws InvalidArgumentsCommandException {
        User user = mock();
        DataBase dataBase = mock();
        List<Song> expected = List.of(new Song("song1", "author1", 8), new Song("song2", "author2", 6));
        when(dataBase.searchSongsByKeywords(any())).thenReturn(expected);
        Command command = new SearchCommand(user, List.of("word"));
        String expectedString =
            "[Song: song1 -- Author: author1 -- Number of listeners: 8, Song: song2 -- Author: author2 -- Number of listeners: 6]" +
                System.lineSeparator();
        assertEquals(expectedString, command.execute(dataBase), "The response is different from the actual");
    }
}
