package bg.sofia.uni.fmi.mjt.music;

import javax.sound.sampled.AudioFormat;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Song {

    private final String songName;
    private final String author;
    private AtomicInteger numberOfListeners;

    private AudioFormat.Encoding encoding;
    private float sampleRate;
    private int sampleSizeInBits;
    private int channels;
    private int frameSize;
    private float frameRate;
    private boolean bigEndian;

    public void setAudioFormat(AudioFormat audioFormat) {
        this.encoding = audioFormat.getEncoding();
        this.sampleRate = audioFormat.getSampleRate();
        this.sampleSizeInBits = audioFormat.getSampleSizeInBits();
        this.channels = audioFormat.getChannels();
        this.frameRate = audioFormat.getFrameRate();
        this.frameSize = audioFormat.getFrameSize();
        this.bigEndian = audioFormat.isBigEndian();

    }

    public Song(String songName, String author, int numberOfListeners) {
        this.songName = songName;
        this.author = author;
        this.numberOfListeners = new AtomicInteger(numberOfListeners);
    }

    public String getSongName() {
        return songName;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumberOfListeners() {
        return numberOfListeners.get();
    }

    public static Song ofFileLine(String line) {
        String[] tokens = line.split(",");
        String songName = tokens[0];
        String author = tokens[1];
        int numberOfListeners = Integer.parseInt(tokens[2]);
        return new Song(songName, author, numberOfListeners);
    }

    @Override
    public String toString() {
        return "Song: " + songName + " -- Author: " + author + " -- Number of listeners: " + numberOfListeners;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Song that)) {
            return false;
        }

        return that.songName.equals(this.songName) && that.author.equals(this.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songName, author);
    }

    public void listen() {
        numberOfListeners.addAndGet(1);
    }

    public String toAudioFormatString() {
        return encoding.toString() + " " + sampleRate + " " + sampleSizeInBits + " " + channels + " " + frameSize +
            " " + frameRate + " " + bigEndian + " ";
    }

    public int getFrameSize() {
        return frameSize;
    }
}