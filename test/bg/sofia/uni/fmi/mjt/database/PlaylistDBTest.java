package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.exceptions.SongAlreadyAddedIntoPlaylist;
import bg.sofia.uni.fmi.mjt.exceptions.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.Playlist;
import bg.sofia.uni.fmi.mjt.music.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlaylistDBTest {

    private static PlaylistDB playlistDB;

    @BeforeEach
    void initializeReader() throws IOException {
        playlistDB = new PlaylistDB(prepareData());
    }

    @Test
    void testCreatePlaylistSuchPlaylistAlreadyExists() {
        assertThrows(SuchPlaylistAlreadyExistsException.class,
            () -> playlistDB.createPlaylist(new User("user1"), "playlist1"),
            "SuchPlaylistAlreadyExistsException was expected");
    }

    @Test
    void testCreatePlaylistSuccessfully() throws SuchPlaylistAlreadyExistsException {
        User user = new User("user1");
        Optional<Playlist> before = playlistDB.getPlaylistByName(user, "test2");
        playlistDB.createPlaylist(user, "test2");
        Optional<Playlist> after = playlistDB.getPlaylistByName(user, "test2");
        assertTrue(before.isEmpty(), "The returned playlist should not exist");
        assertTrue(after.isPresent(), "The playlist should be created");
    }

    @Test
    void testAddSongToPlaylistNoSuchPlaylistException() {
        assertThrows(NoSuchPlaylistException.class,
            () -> playlistDB.addSongToPlaylist(new User("user1"),
                "name", new Song("test", "test", 2)),
            "NoSuchPlaylistException expected");
    }

    @Test
    void testAddSongToPlaylistSongAlreadyAddedIntoPlaylist() {
        assertThrows(SongAlreadyAddedIntoPlaylist.class,
            () -> playlistDB.addSongToPlaylist(new User("user1"),
                "playlist1", new Song("song1", "author1", 2)),
            "SongAlreadyAddedException expected");
    }

    @Test
    void testAddSongToPlaylistSuccessfully() throws SongAlreadyAddedIntoPlaylist, NoSuchPlaylistException {
        Song song = new Song("newsong", "newauthor", 10);
        playlistDB.addSongToPlaylist(new User("user1"), "playlist1", song);
        Optional<Playlist> playlist = playlistDB.getPlaylistByName(new User("user1"), "playlist1");
        playlist.ifPresent(value -> assertTrue(value.containsSong(song), "The song was not added"));
    }

    /* @Test
     void testSingleton() throws IOException {
         Reader reader = new StringReader("");
         PlaylistDB playlistDB1 = PlaylistDB.getInstance(reader);
         PlaylistDB playlistDB2 = PlaylistDB.getInstance(reader);
         assertSame(playlistDB1, playlistDB2, "The references must be the same");
     }
 */
    private Map<User, List<Playlist>> prepareData() throws IOException {
        Map<User, List<Playlist>> data = new HashMap<>();
        SongsDB songsDB = mock();
        when(songsDB.getSongBySongName("song1")).thenReturn(Optional.of(new Song("song1", "author1", 5)));
        when(songsDB.getSongBySongName("song2")).thenReturn(Optional.of(new Song("song2", "author2", 10)));
        Playlist playlist1 = Playlist.of(songsDB, "user1:playlist1:song1,song2");
        Playlist playlist2 = Playlist.of(songsDB, "user2:playlist1:song1");
        List<Playlist> first = new ArrayList<>();
        List<Playlist> second = new ArrayList<>();
        first.add(playlist1);
        second.add(playlist2);
        data.putIfAbsent(playlist1.getUser(), first);
        data.putIfAbsent(playlist2.getUser(), second);
        return data;
    }
}
