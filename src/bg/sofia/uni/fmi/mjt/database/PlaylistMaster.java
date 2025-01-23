package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.exceptions.SongAlreadyAddedIntoPlaylist;
import bg.sofia.uni.fmi.mjt.exceptions.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.Playlist;
import bg.sofia.uni.fmi.mjt.music.Song;

import java.util.Optional;

public interface PlaylistMaster {
    void createPlaylist(User user, String playlistName) throws SuchPlaylistAlreadyExistsException;

    void addSongToPlaylist(User user, String playlistName, Song song)
        throws NoSuchPlaylistException, SongAlreadyAddedIntoPlaylist;

    Optional<Playlist> getPlaylistByName(User user, String playlistName);
}
