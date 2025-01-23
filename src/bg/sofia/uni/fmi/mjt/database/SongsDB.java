package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.music.Song;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SongsDB implements SongMaster {

    private final Set<Song> songs;

    private static SongsDB instance = null;

    private SongsDB(Reader reader) throws IOException {
        try (BufferedReader br = new BufferedReader(reader)) {
            songs = br
                .lines()
                .map(Song::ofFileLine)
                .collect(Collectors.toSet());
        }
    }

    public static SongsDB getInstance(Reader reader) throws IOException {
        if (instance == null) {
            instance = new SongsDB(reader);
        }
        return instance;
    }

    @Override
    public Collection<Song> getAllAvailableSongs() {
        return songs;
    }

    @Override
    public Collection<Song> searchSongsByKeywords(String... words) {
        if (words == null) {
            throw new IllegalArgumentException("Words is null, but it should not");
        }

        Collection<Song> result = new HashSet<>();
        songs
            .forEach(song -> {
                for (String word : words) {
                    if (song.getSongName().contains(word) || song.getAuthor().contains(word)) {
                        result.add(song);
                    }
                }
            });
        return result;
    }

    @Override
    public List<Song> getMostTrendingSongs(int numberOfTrendingSongs) {
        if (numberOfTrendingSongs <= 0) {
            throw new IllegalArgumentException("The number of songs must be non-negative");
        }

        return songs.stream()
            .sorted(Comparator.comparingInt(Song::getNumberOfListeners).reversed())
            .limit(numberOfTrendingSongs)
            .toList();
    }

    @Override
    public Optional<Song> getSongBySongName(String songName) {
        return songs.stream().filter(song -> song.getSongName().equals(songName)).findFirst();
    }

    public void saveSongs(Writer writer) {
        try (PrintWriter printWriter = new PrintWriter(writer)) {
            for (Song song : songs) {
                printWriter.println(song.toString());
                printWriter.flush();
            }
        }
    }
}
