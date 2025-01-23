package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.access.AccountAuthenticator;
import bg.sofia.uni.fmi.mjt.access.AccountRegistration;
import bg.sofia.uni.fmi.mjt.access.Loginable;
import bg.sofia.uni.fmi.mjt.access.Registrable;
import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.exceptions.FailedDataBaseConnectionException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchSongNameException;
import bg.sofia.uni.fmi.mjt.exceptions.SongAlreadyAddedIntoPlaylist;
import bg.sofia.uni.fmi.mjt.exceptions.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.Playlist;
import bg.sofia.uni.fmi.mjt.music.Song;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DataBase implements DataBaseService {

    private static DataBase instance = null;

    private final SongsDB songs;
    private final PlaylistDB playlists;
    private final Registrable registrator;
    private final Loginable authenticator;
    private static final String USERS_FILE_NAME = "./accounts.txt";
    private static final String PLAYLISTS_FILE_NAME = "./playlists.txt";
    private static final String SONGS_FILE_NAME = "./songs.txt";

    private DataBase(Reader songsReader, Reader usersReader) throws FailedDataBaseConnectionException {
        try {
            songs = SongsDB.getInstance(new FileReader(SONGS_FILE_NAME));
            playlists = PlaylistDB.getInstance(new FileReader("./playlists.txt"));
            registrator = new AccountRegistration();
            authenticator = new AccountAuthenticator();
        } catch (IOException e) {
            throw new FailedDataBaseConnectionException("Unable to load data from database");
        }
    }

    public static DataBase getInstance() throws FailedDataBaseConnectionException {
        if (instance == null) {
            try {
                instance = new DataBase(new FileReader(SONGS_FILE_NAME), new FileReader(USERS_FILE_NAME));
            } catch (FileNotFoundException e) {
                throw new FailedDataBaseConnectionException("Problem with storage file");
            }
        }
        return instance;
    }

    public Collection<Song> searchSongsByKeywords(String... words) {
        return songs.searchSongsByKeywords(words);
    }

    public Collection<Song> getAllAvailableSongs() {
        return songs.getAllAvailableSongs();
    }

    public List<Song> getMostTrendingSongs(int numberOfTrendingSongs) {
        return songs.getMostTrendingSongs(numberOfTrendingSongs);
    }

    public boolean createPlaylist(User user, String playlistName) throws SuchPlaylistAlreadyExistsException {
        playlists.createPlaylist(user, playlistName);
        return true;
    }

    public boolean addSongToPlaylist(User user, String playlistName, String songName)
        throws NoSuchSongNameException, SongAlreadyAddedIntoPlaylist, NoSuchPlaylistException {

        Optional<Song> toAdd = songs.getSongBySongName(songName);
        if (toAdd.isEmpty()) {
            throw new NoSuchSongNameException("There is no such song in out database");
        }
        playlists.addSongToPlaylist(user, playlistName, toAdd.get());
        return true;
    }

    public Collection<Song> showPlaylistInfo(User user, String playlistName) throws NoSuchPlaylistException {
        Optional<Playlist> playlist = playlists.getPlaylistByName(user, playlistName);
        if (playlist.isEmpty()) throw new NoSuchPlaylistException("There is no such playlist");
        return playlist.get().getSongs();
    }

    public Optional<Song> getSongBySongName(String songName) {
        return songs.getSongBySongName(songName);
    }

    public Registrable getRegistrator() {
        return registrator;
    }

    public Loginable getAuthenticator() {
        return authenticator;
    }

    public void saveSnapshot() throws IOException {
        songs.saveSongs(new FileWriter(SONGS_FILE_NAME));
        playlists.savePlaylists(new FileWriter(PLAYLISTS_FILE_NAME));
    }
}
