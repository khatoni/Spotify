package bg.sofia.uni.fmi.mjt.command.commands;

import bg.sofia.uni.fmi.mjt.access.User;
import bg.sofia.uni.fmi.mjt.database.DataBase;
import bg.sofia.uni.fmi.mjt.exceptions.InvalidArgumentsCommandException;
import bg.sofia.uni.fmi.mjt.music.Song;
import bg.sofia.uni.fmi.mjt.server.ServerResponse;
import bg.sofia.uni.fmi.mjt.server.SpotifyServer;
import bg.sofia.uni.fmi.mjt.stream.SongStreamer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PLaySongCommand implements Command {
    private final User attachedUser;
    private final String songName;

    public PLaySongCommand(User user, List<String> arguments) throws InvalidArgumentsCommandException {
        this.attachedUser = user;
        this.songName = arguments.get(0);
    }

    @Override
    public String execute(DataBase dbConnection) {
        if (attachedUser == null) {
            return ServerResponse.NOT_AUTHENTICATED;
        }
        try {
            Optional<Song> song = dbConnection.getSongBySongName(songName);
            if (song.isEmpty()) {
                return "No such song";
            }
            AudioFormat audioFormat =
                AudioSystem.getAudioInputStream(new File("./songs/" + song.get().getSongName() + ".wav")).getFormat();

            song.get().setAudioFormat(audioFormat);
            int port = SpotifyServer.getInstance().getFreePort();
            Runnable streamer = new SongStreamer(song.get(), port);
            Thread toExecute = new Thread(streamer);
            //toExecute.setDaemon(true);
            toExecute.start();
            return song.get().toAudioFormatString() + port + System.lineSeparator();
        } catch (UnsupportedAudioFileException | IOException e) {
            return ServerResponse.FAILED_OPERATION;
        }

    }
}
