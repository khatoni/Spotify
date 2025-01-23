package bg.sofia.uni.fmi.mjt.music;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.SongsDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Playlist {
    private final User user;
    private final String playlistName;

    private List<Song> songs;

    public Playlist(User user, String playlistName, List<Song> songs) {
        this.user = user;
        this.playlistName = playlistName;
        this.songs = songs;
    }

    public Playlist(User user, String playlistName) {
        if (user == null || playlistName == null) {
            throw new IllegalArgumentException("Trying to initialize playlist with invalid data");
        }
        this.user = user;
        this.playlistName = playlistName;
        songs = new ArrayList<>();
    }

    public static Playlist of(SongsDB songsDB, String line) throws IOException {
        List<Song> songs = new ArrayList<>();
        String[] tokens = line.split(":");
        User user = new User(tokens[0]);
        String playlistName = tokens[1];
        String[] songNames = tokens[2].split(",");
        for (var song : songNames) {
            songs.add(songsDB.getSongBySongName(song).get());
        }
        return new Playlist(user, playlistName, songs);
    }

    public void addSongToPlaylist(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Trying to add illegal song to playlist");
        }
        songs.add(song);
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public User getUser() {
        return user;
    }

    public Collection<Song> getSongs() {
        return songs;
    }

    public boolean containsSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("The song is null");
        }
        return songs.contains(song);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Playlist that)) {
            return false;
        }

        return that.user.equals(this.user) && that.playlistName.equals(this.playlistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, playlistName);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(playlistName + ":");
        for (Song song : songs) {
            result.append(song.getSongName()).append(",");
        }
        return result.toString();
    }
}
