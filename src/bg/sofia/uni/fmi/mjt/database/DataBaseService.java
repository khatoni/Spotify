package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchSongNameException;
import bg.sofia.uni.fmi.mjt.exceptions.SongAlreadyAddedIntoPlaylist;
import bg.sofia.uni.fmi.mjt.exceptions.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.Song;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DataBaseService {

    Collection<Song> searchSongsByKeywords(String... words);

    Collection<Song> getAllAvailableSongs();

    List<Song> getMostTrendingSongs(int numberOfTrendingSongs);

    boolean createPlaylist(User user, String playlistName) throws SuchPlaylistAlreadyExistsException;

    boolean addSongToPlaylist(User user, String playlistName, String songName)
        throws NoSuchSongNameException, SongAlreadyAddedIntoPlaylist, NoSuchPlaylistException;

    Collection<Song> showPlaylistInfo(User user, String playlistName) throws NoSuchPlaylistException;

    Optional<Song> getSongBySongName(String songName);
}
