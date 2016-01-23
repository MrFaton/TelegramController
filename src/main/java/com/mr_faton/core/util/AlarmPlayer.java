package com.mr_faton.core.util;

import org.apache.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class AlarmPlayer {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.util.AlarmPlayer");
    private Clip clip;
    private boolean isAlreadyPlaying = false;

    public void play() {
        File alarmSoundFile = new File(SettingsHolder.getAlarmSoundPath());

        if (!alarmSoundFile.exists()) {
            logger.debug("Can't play alarm because alarm file is not exists");
            return;
        }

        logger.debug("play alarm");

        if (isAlreadyPlaying) {
            return;
        }

        isAlreadyPlaying = true;

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(alarmSoundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            logger.warn("Alarm Player exception", ex);
        }
    }

    public void stop() {
        logger.debug("stop playing alarm");
        if (clip != null && clip.isRunning()) {
            isAlreadyPlaying = false;
            clip.stop();
        }
    }
}