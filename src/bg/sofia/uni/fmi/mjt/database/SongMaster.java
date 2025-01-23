package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.music.Song;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SongMaster {

    Collection<Song> getAllAvailableSongs();

    Collection<Song> searchSongsByKeywords(String... words);

    List<Song> getMostTrendingSongs(int n);

    Optional<Song> getSongBySongName(String songName);
}
