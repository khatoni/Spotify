package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.exceptions.SongAlreadyAddedIntoPlaylist;
import bg.sofia.uni.fmi.mjt.exceptions.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.Playlist;
import bg.sofia.uni.fmi.mjt.music.Song;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlaylistDB {

    private static PlaylistDB instance;

    private Map<User, List<Playlist>> data;

    private PlaylistDB(Reader reader) throws IOException {
        try (BufferedReader br = new BufferedReader(reader)) {
            SongsDB dbConnection = SongsDB.getInstance(new FileReader("./songs.txt"));
            data = br
                .lines()
                .map(element -> {
                    try {
                        return Playlist.of(dbConnection, element);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.groupingBy(Playlist::getUser));
        }
    }

    public PlaylistDB(Map<User, List<Playlist>> playlists) {
        this.data = playlists;
    }

    public static PlaylistDB getInstance(Reader reader) throws IOException {
        if (instance == null) {
            instance = new PlaylistDB(reader);
        }
        return instance;
    }

    private Optional<Playlist> checkPlaylistExists(User user, String playlistName) {
        return data.get(user).stream().filter(playlist -> playlist.getPlaylistName().equals(playlistName)).findFirst();
    }

    public void createPlaylist(User user, String playlistName) throws SuchPlaylistAlreadyExistsException {
        data.putIfAbsent(user, new ArrayList<>());
        if (checkPlaylistExists(user, playlistName).isPresent()) {
            throw new SuchPlaylistAlreadyExistsException("Such playlist already exists");
        }
        data.get(user).add(new Playlist(user, playlistName));
    }

    public void addSongToPlaylist(User user, String playlistName, Song song)
        throws NoSuchPlaylistException, SongAlreadyAddedIntoPlaylist {
        Optional<Playlist> userPlaylist = checkPlaylistExists(user, playlistName);
        if (userPlaylist.isEmpty()) {
            throw new NoSuchPlaylistException("There is no playlist with this name");
        }

        if (userPlaylist.get().containsSong(song)) {
            throw new SongAlreadyAddedIntoPlaylist("This song is already added into the playlist");
        }
        userPlaylist.get().addSongToPlaylist(song);
    }

    public Optional<Playlist> getPlaylistByName(User user, String playlistName) {
        return checkPlaylistExists(user, playlistName);
    }

    public void savePlaylists(Writer writer) {
        try (PrintWriter printWriter = new PrintWriter(writer)) {
            for (User user : data.keySet()) {
                Collection<Playlist> userCollection = data.get(user);
                for (Playlist playlist : userCollection) {
                    printWriter.println(user.getUsername() + ":" + playlist.toString());
                }
            }
        }
    }
}
