package com.mr_faton.core.util;

import com.mr_faton.core.entity.DBServer;
import com.mr_faton.core.entity.Telegram;
import com.mr_faton.core.exception.EmptyListException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SettingsHolder {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.util.SettingsHolder");
    private static final String TELEGRAM_LIST_FILE = "TelegramList.ser";
    private static final String DB_SERVER_LIST_FILE = "DBServerList.ser";
    private static final String SETTINGS_FILE = "Settings.txt";
    private static List<Telegram> telegramList = null;
    private static List<DBServer> dbServerList = null;

    // ***** Load / Save *****
    public static void load() throws Exception {
        logger.debug("loading telegram list and db server list");
        try {
            telegramList = (List<Telegram>) loadObject(TELEGRAM_LIST_FILE);
            dbServerList = (List<DBServer>) loadObject(DB_SERVER_LIST_FILE);
        } catch (FileNotFoundException fileEx) {
            logger.debug("file with telegrams or db servers not found");
        } catch (Exception e) {
            logger.warn("exception while loading lists", e);
            throw e;
        }
    }

    public static void save() throws Exception {
        logger.debug("save telegram list and db server list");
        try {
            saveObject(telegramList, TELEGRAM_LIST_FILE);
            saveObject(dbServerList, DB_SERVER_LIST_FILE);
        } catch (Exception e) {
            logger.warn("exception while saving lists", e);
            throw e;
        }
    }

    // ***** Setters *****
    public static void updateTelegramList(List<Telegram> updatedTelegramList) {
        logger.debug("updating telegram list");
        telegramList = updatedTelegramList;
    }

    public static void updateDBServerList(List<DBServer> updatedDBServerList) {
        logger.debug("updating db server list");
        dbServerList = updatedDBServerList;
    }

    // ***** Getters *****
    public static List<Telegram> getTelegramList() {
        logger.debug("return telegram list");
        return telegramList;
    }

    public static List<Telegram> getEnableTelegramList() throws EmptyListException {
        if (telegramList == null || telegramList.isEmpty()) {
            logger.warn("telegram list (" + telegramList + ") = null or empty");
            throw new EmptyListException("Telegram List is empty or null");
        }

        List<Telegram> enableTelegramList = new ArrayList<>();
        for (Telegram telegram : telegramList) {
            if (telegram.getState()) enableTelegramList.add(telegram);
        }
        logger.debug("return enabled telegram list");
        return enableTelegramList;
    }

    public static List<DBServer> getDbServerList() {
        logger.debug("return db server list list");
        return dbServerList;
    }

    public static List<DBServer> getEnabledDbServerList() throws EmptyListException {
        if (dbServerList == null || dbServerList.isEmpty()) {
            logger.warn("dbServerList list (" + dbServerList + ") = null or empty");
            throw new EmptyListException("DB Server List is empty or null");
        }
        List<DBServer> enabledDBServerList = new ArrayList<>();
        for (DBServer dbServer : dbServerList) {
            if (dbServer.getState()) enabledDBServerList.add(dbServer);
        }

        logger.debug("return enabled db server list");
        return enabledDBServerList;
    }

    public static String getAlarmSoundPath() {
        String alarmSoundPath;
        try (InputStream in = new FileInputStream(SETTINGS_FILE)) {
            Properties properties = new Properties();
            properties.load(in);
            alarmSoundPath = properties.getProperty("ALARM_SOUND_PATH");
        } catch (IOException ex) {
            alarmSoundPath = "";
        }
        return alarmSoundPath;
    }

    public static void saveAlarmSoundPath(String soundPath) {
        try (OutputStream out = new FileOutputStream(SETTINGS_FILE)) {
            Properties properties = new Properties();
            properties.put("ALARM_SOUND_PATH", soundPath);
            properties.store(out, "Save or update alarm sound path");
        } catch (IOException ex) {
            logger.warn("Can't save alarm sound path", ex);
        }
    }

    //     ***** Service *****
    private static Object loadObject(String FILE_NAME) throws Exception {
        try (ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return objectIn.readObject();
        }
    }

    private static void saveObject(Object object, String FILE_NAME) throws Exception {
        try (ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOut.writeObject(object);
            objectOut.flush();
        }
    }
}
