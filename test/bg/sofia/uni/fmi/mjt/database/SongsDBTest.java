package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.music.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SongsDBTest {
    private static final String data = """
        song1,author author,5
        song2,author2,3
        song3,author3,7
        """;

    private static Reader reader;
    private static SongsDB songsDB;

    @BeforeEach
    void initializeReader() throws IOException {
        reader = new StringReader(data);
        songsDB = SongsDB.getInstance(reader);
    }

    @Test
    void testGetAllAvailableSongs() throws IOException {
        Collection<Song> expected = prepareExpectedResult();
        assertTrue(assertEqualHashSets(expected, songsDB.getAllAvailableSongs()),
            "The expected collection is different from the received");
    }

    @Test
    void testSearchSongsByKeywords() throws IOException {
        Collection<Song> expected = prepareExpectedResult();
        assertTrue(assertEqualHashSets(expected, songsDB.searchSongsByKeywords("author", "song2", "song3")),
            "The expected collection is different from the received");
    }

    @Test
    void testMostTrendingSongsNegativeNumber() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> songsDB.getMostTrendingSongs(-1),
            "IllegalArgumentException was expected");
    }

    @Test
    void testMostTrendingSongsTopThree() throws IOException {
        Collection<Song> expected = prepareSortedByTrend();
        assertTrue(assertEqualHashSets(expected, songsDB.getMostTrendingSongs(3)),
            "The expected collection is different from the received");
    }

    @Test
    void testGetSongBySongName() throws IOException {
        Optional<Song> expected = Optional.of(Song.ofFileLine("song3,author3,7"));
        assertEquals(expected.get(), songsDB.getSongBySongName("song3").get(),
            "The expected song by name is not the same");
    }

    private Collection<Song> prepareSortedByTrend() {
        Collection<Song> expected = new HashSet<>();
        expected.add(Song.ofFileLine("song3,author3,7"));
        expected.add(Song.ofFileLine("song1,author author,5"));
        expected.add(Song.ofFileLine("song2,author2,3"));
        return expected;
    }

    private Collection<Song> prepareExpectedResult() {
        Collection<Song> expected = new HashSet<>();
        expected.add(Song.ofFileLine("song1,author author,5"));
        expected.add(Song.ofFileLine("song2,author2,3"));
        expected.add(Song.ofFileLine("song3,author3,7"));
        return expected;
    }

    private boolean assertEqualHashSets(Collection<Song> left, Collection<Song> right) {
        if (left.size() != right.size()) {
            return false;
        }
        return left.containsAll(right);
    }

    @AfterEach
    void closeReader() throws IOException {
        reader.close();
    }
}
