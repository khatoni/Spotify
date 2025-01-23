package bg.sofia.uni.fmi.mjt.music;

import bg.sofia.uni.fmi.mjt.access.User;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SongTest {

    @Test
    void testEqualSongtDifferetnTypes() {
        Song first = new Song("song1", "author1", 2);
        assertNotEquals(first, new Object(), "The object should not be equal to song");
    }

    @Test
    void testEqualSong() {
        Song first = new Song("song1", "author1", 2);
        Song second = new Song("song1", "author1", 5);

        assertEquals(first, second, "The songs should  be equal");
    }

    @Test
    void testHashCode() {
        Song first = new Song("song1", "author1", 2);
        Song second = new Song("song1", "author1", 5);
        assertEquals(first.hashCode(), second.hashCode(), "The hashcodes of two equal object must be the same");
    }

    @Test
    void testListen() {
        Song toTest = new Song("song1", "author1", 2);
        int numberOfListenersBefore = toTest.getNumberOfListeners();
        toTest.listen();
        int numberOfListenersAfter = toTest.getNumberOfListeners();
        assertEquals(numberOfListenersAfter - 1, numberOfListenersBefore, "Expected difference of 1");
    }

    @Test
    void testToAudioFormatString() {
        Song toTest = new Song("song1", "author1", 2);
        AudioFormat audioFormat = prepareAudio();
        toTest.setAudioFormat(audioFormat);
        String expectedOutput = "PCM_SIGNED 2.0 8 4 4 2.0 true ";
        assertEquals(expectedOutput, toTest.toAudioFormatString(), "The two strings should be the same");
    }

    private AudioFormat prepareAudio() {
        AudioFormat audioFormat = mock();
        when(audioFormat.getEncoding()).thenReturn(AudioFormat.Encoding.PCM_SIGNED);
        when(audioFormat.getChannels()).thenReturn(4);
        when(audioFormat.getFrameRate()).thenReturn(2.0f);
        when(audioFormat.getFrameSize()).thenReturn(4);
        when(audioFormat.getSampleRate()).thenReturn(2.0f);
        when(audioFormat.getSampleSizeInBits()).thenReturn(8);
        when(audioFormat.isBigEndian()).thenReturn(true);
        return audioFormat;
    }
}
