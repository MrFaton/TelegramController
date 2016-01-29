package com.mr_faton.core.util;

import org.apache.log4j.Logger;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

public class AlarmPlayer {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.util.AlarmPlayer");
    private AudioClip clip;
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
            clip = Applet.newAudioClip(alarmSoundFile.toURI().toURL());
            clip.loop();
        } catch (MalformedURLException ex) {
            logger.warn("Alarm Player exception", ex);
        }
    }

    public void stop() {
        logger.debug("stop playing alarm");
        isAlreadyPlaying = false;
        if (clip != null) {
            clip.stop();
            clip = null;
        }
    }
}