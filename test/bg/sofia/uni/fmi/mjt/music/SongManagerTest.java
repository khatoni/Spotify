package bg.sofia.uni.fmi.mjt.music;

import bg.sofia.uni.fmi.mjt.music.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/*
public class SongManagerTest {

    private static Reader songsReader;

    @BeforeEach
    void initReader() {
        String data = """
            Bohemian Rhapsody,Queen,10
            The show must go on,Queen,13
            Deutchland,Rammstein,5
            Bellyache,Billie Aillish,3
            Bored,Billie Aillish,7
            Ashes,Celine Dion,12
            Komanda,Egor Kreed Polina Gagarina,9
            """;
        songsReader = new StringReader(data);
    }

    @Test
    void testReadSongsFromFile() {
        SongManager songManager = new SongManager(songsReader);
        List<Song> expectedSongsList = new ArrayList<>();
        expectedSongsList.add(Song.ofFileLine("Bohemian Rhapsody,Queen,10"));
        expectedSongsList.add(Song.ofFileLine("The show must go on,Queen,12"));
        expectedSongsList.add(Song.ofFileLine("Deutchland,Rammstein,5"));
        expectedSongsList.add(Song.ofFileLine("Bellyache,Billie Aillish,3"));
        expectedSongsList.add(Song.ofFileLine("Bored,Billie Aillish,7"));
        expectedSongsList.add(Song.ofFileLine("Ashes,Celine Dion,12"));
        expectedSongsList.add(Song.ofFileLine("Komanda,Egor Kreed Polina Gagarina,9"));
        assertIterableEquals(expectedSongsList, songManager.getAllAvailableSongs(),
            "The returned collection of songs is not the same as the expected list of songs");
    }

    @Test
    void testSearchSongByKeywordsNullKeywords() {
        assertThrows(IllegalArgumentException.class, () -> new SongManager(songsReader).searchSongsByKeywords(null),
            "IllegalArgumentException was expected");
    }

    @Test
    void testSearchSongByKeywordsOneKeywordAuthor() {
        SongManager songManager = new SongManager(songsReader);
        String keyword = "Dion";
        List<Song> expected = new ArrayList<>();
        expected.add(Song.ofFileLine("Ashes,Celine Dion,12"));
        assertIterableEquals(expected, songManager.searchSongsByKeywords(keyword),
            "The expected list differs from the received");
    }

    @Test
    void searchSongByMultipleKeywords() {
        SongManager songManager = new SongManager(songsReader);
        List<Song> expected = new ArrayList<>();
        expected.add(Song.ofFileLine("Bohemian Rhapsody,Queen,10"));
        expected.add(Song.ofFileLine("The show must go on,Queen,12"));
        expected.add(Song.ofFileLine("Bored,Billie Aillish,7"));
        assertIterableEquals(expected, songManager.searchSongsByKeywords("Queen", "Bore"),
            "The expected list differs from the received");
    }

    @Test
    void testGetMostTrendingSongsNegativeNumberOfSongs() {
        assertThrows(IllegalArgumentException.class, () -> new SongManager(songsReader).getMostTrendingSongs(-2),
            "The number of trending songs must be non-negative");
    }

    @Test
    void testGetMostTrendingSongsTop3Songs() {
        SongManager songManager = new SongManager(songsReader);
        List<Song> expected = new ArrayList<>();
        expected.add(Song.ofFileLine("The show must go on,Queen,13"));
        expected.add(Song.ofFileLine("Ashes,Celine Dion,12"));
        expected.add(Song.ofFileLine("Bohemian Rhapsody,Queen,10"));
        assertIterableEquals(expected, songManager.getMostTrendingSongs(3),
            "The expected list differs from the received");
    }
}*/
