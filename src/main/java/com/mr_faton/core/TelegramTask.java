package com.mr_faton.core;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.TelegramControllerDAO;
import com.mr_faton.core.entity.DBServer;
import com.mr_faton.core.entity.Telegram;
import com.mr_faton.core.util.AlarmPlayer;
import com.mr_faton.gui.notifier.UserNotifier;
import com.mr_faton.gui.panel.ButtonPanel;
import com.mr_faton.gui.panel.NotificationPanel;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 06.10.2015
 */
public class TelegramTask implements Runnable {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.TelegramTask");
    private final Telegram telegram;
    private final DBServer dbServer;
    private final TelegramControllerDAO telegramControllerDAO;
    private final NotificationPanel notificationPanel;
    private final ButtonPanel buttonPanel;
    private final AlarmPlayer alarmPlayer;

    public TelegramTask(Telegram telegram, DBServer dbServer) {
        this.telegram = telegram;
        this.dbServer = dbServer;
        this.telegramControllerDAO = (TelegramControllerDAO) AppContext.getBeanByName("telegramControllerDAO");
        this.notificationPanel = (NotificationPanel) AppContext.getBeanByName("notificationPanel");
        this.buttonPanel = (ButtonPanel) AppContext.getBeanByName("buttonPanel");
        this.alarmPlayer = (AlarmPlayer) AppContext.getBeanByName("alarmPlayer");
    }

    @Override
    public void run() {
        try {
            if (telegramControllerDAO.isTelegramExist(telegram, dbServer)) {
                String logMessage = "Телеграмма \"" + telegram + "\" есть в БД \"" + dbServer + "\"";
                logger.info(logMessage);
                notificationPanel.addNotification(logMessage);
            } else {
                String logMessage = "Телеграмма \"" + telegram + "\" в БД \"" + dbServer + "\" НЕ существует!";
                logger.warn(logMessage);
                notificationPanel.addWarningNotification(logMessage);
                alarmPlayer.play();
                buttonPanel.disableStopButton();
                buttonPanel.enableNotifiedButton();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserNotifier.errorMessage("Пропущена телеграмма",
                                "Была пропущена телеграмма, ознакомьтесь с деталями!");
                    }
                }).start();
            }
        } catch (SQLException e) {
            String logMessage = "Возникла ошибка при работе с базой данных! Пожалуйста, завершите приложение!";
            logger.error(logMessage, e);
            notificationPanel.addWarningNotification(logMessage);
            notificationPanel.addWarningNotification("Детали: " + e.getMessage());
            UserNotifier.errorMessage("Ошибка ошибка при работе с базой данных!", e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "TelegramTask for \"" + telegram + "\" and \"" + dbServer + "\"";
    }
}
