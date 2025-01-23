package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.command.commands.Command;
import bg.sofia.uni.fmi.mjt.command.commands.TopSongsCommand;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.music.Song;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TopSongsCommandTest {

    @Test
    void testExecuteCommandNoSession() throws InvalidArgumentsCommandException {
        Command command = new TopSongsCommand(null, List.of("10"));
        DataBase dataBase = mock();
        String response = command.execute(dataBase);
        assertEquals(ServerResponse.NOT_AUTHENTICATED, response,
            "The expected response is that user is not authenticated");
    }

    @Test
    void testCreateCommandWithInvalidNumberOfArguments() {
        assertThrows(InvalidArgumentsCommandException.class, () -> new TopSongsCommand(null, List.of("10", "20")),
            "InvalidArgumentsCommandException was expected");
    }

    @Test
    void testCommandExecuteSuccessfully() throws InvalidArgumentsCommandException {
        User user = mock();
        Command command = new TopSongsCommand(user, List.of("2"));
        DataBase dataBase = mock();
        List<Song> expected = List.of(new Song("song1", "author1", 8), new Song("song2", "author2", 6));
        when(dataBase.getMostTrendingSongs(2)).thenReturn(expected);
        String expectedString =
            "[Song: song1 -- Author: author1 -- Number of listeners: 8, Song: song2 -- Author: author2 -- Number of listeners: 6]" +
                System.lineSeparator();
        assertEquals(expectedString, command.execute(dataBase), "The response is different from the actual");
    }
}
