package bg.sofia.uni.fmi.mjt.stream;

import bg.sofia.uni.fmi.mjt.music.Song;
import bg.sofia.uni.fmi.mjt.server.SpotifyServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SongStreamer implements Runnable {

    private final int port;
    private Song song;

    public SongStreamer(Song song, int port) {
        this.port = port;
        this.song = song;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try (Socket socket = serverSocket.accept();
                 BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(
                     Path.of("./songs/" + song.getSongName() + ".wav")))) {

                byte[] toWrite = new byte[song.getFrameSize()];
                while (bufferedInputStream.available() > 0) {
                    int readBytes = bufferedInputStream.read(toWrite, 0, toWrite.length);
                    outputStream.write(toWrite, 0, readBytes);
                }

                outputStream.flush();
            } catch (SocketException ignored) {
                //The User has Stopped The Song
            }
        } catch (IOException e) {
            System.out.println("A Problem occurred while streaming Song");
        }

        song.listen();
        SpotifyServer.getInstance().addFreePort(port);
        System.out.println("Song has ended");
    }
}
