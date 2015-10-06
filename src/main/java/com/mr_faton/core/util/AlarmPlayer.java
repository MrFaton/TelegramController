package com.mr_faton.core.util;

import org.apache.log4j.Logger;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.IOException;
import java.io.InputStream;

public class AlarmPlayer {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.util.AlarmPlayer");
    private static final String FILE = "Alarm.wav";
    private InputStream inputStream;
    private AudioStream audioStream;

    public void play() {
        logger.debug("play alarm");
        try {
            inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(FILE);
            audioStream = new AudioStream(inputStream);
        } catch (IOException e) {
            logger.warn("Alarm Player exception", e);
        }
        AudioPlayer.player.start(audioStream);
    }

    public void stop() {
        logger.debug("stop playing alarm");
        AudioPlayer.player.stop(audioStream);
        try {
            audioStream.close();
            inputStream.close();
        } catch (IOException e) {
            logger.warn("Alarm Player exception", e);
        }

    }
}