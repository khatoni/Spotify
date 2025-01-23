package bg.sofia.uni.fmi.mjt.music;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.SongsDB;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlaylistTest {

    @Test
    void testCreatingOfPlaylistFromString() throws IOException {
        SongsDB songsDB = mock();
        Song first = new Song("song1", "author1", 2);
        Song second = new Song("song2", "author2", 5);
        List<Song> expectedList = List.of(first, second);
        when(songsDB.getSongBySongName("song1"))
            .thenReturn(Optional.of(first));
        when(songsDB.getSongBySongName("song2"))
            .thenReturn(Optional.of(second));
        String data = "user1:playlist1:song1,song2";
        Playlist toTest = Playlist.of(songsDB, data);
        Playlist expected = new Playlist(new User("user1"), "playlist1", expectedList);
        assertEquals(expected, toTest, "The playlist from file differs from the expected one");
    }

    @Test
    void testEqualPlaylistDifferetnTypes() {
        Song first = new Song("song1", "author1", 2);
        Song second = new Song("song2", "author2", 5);
        Playlist expected = new Playlist(new User("user1"), "playlist1", List.of(first, second));
        assertNotEquals(expected, first, "The playlist should not be equal to song");
    }

    @Test
    void testEqualPlaylist() {
        Song first = new Song("song1", "author1", 2);
        Song second = new Song("song2", "author2", 5);
        Playlist left = new Playlist(new User("user1"), "playlist1", List.of(first, second));
        Playlist right = new Playlist(new User("user1"), "playlist1", List.of(first,second));
        assertEquals(left, right, "The playlist should  be equal to song");
    }

    @Test
    void testSameReference() {
        Song first = new Song("song1", "author1", 2);
        Song second = new Song("song2", "author2", 5);
        Playlist left = new Playlist(new User("user1"), "playlist1", List.of(first, second));
        assertEquals(left, left, "The two references must be equal");
    }

    @Test
    void testHashCode() {
        Song first = new Song("song1", "author1", 2);
        Song second = new Song("song2", "author2", 5);
        Playlist left = new Playlist(new User("user1"), "playlist1", List.of(first, second));
        Playlist right = new Playlist(new User("user1"), "playlist1", List.of(first,second));
        assertEquals(left.hashCode(), right.hashCode(), "The hashcodes of two equal object must be the same");
    }
}
