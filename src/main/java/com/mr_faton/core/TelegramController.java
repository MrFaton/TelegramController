package com.mr_faton.core;

import com.mr_faton.core.entity.DBServer;
import com.mr_faton.core.entity.Telegram;
import com.mr_faton.core.exception.EmptyListException;
import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.gui.notifier.UserNotifier;
import com.mr_faton.gui.panel.ButtonPanel;
import com.mr_faton.gui.panel.NotificationPanel;
import it.sauronsoftware.cron4j.Scheduler;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 06.10.2015
 */
public class TelegramController implements Runnable {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.TelegramController");
    private List<Telegram> enabledTelegrams;
    private List<DBServer> enabledDBServers;
    private final NotificationPanel notificationPanel;
    private final ButtonPanel buttonPanel;
    private final Scheduler scheduler;

    public TelegramController(NotificationPanel notificationPanel, ButtonPanel buttonPanel)
            throws EmptyListException {

        this.notificationPanel = notificationPanel;
        this.buttonPanel = buttonPanel;
        scheduler = new Scheduler();

        try {
            enabledTelegrams = SettingsHolder.getEnableTelegramList();
            enabledDBServers = SettingsHolder.getEnabledDbServerList();
        } catch (EmptyListException ex) {
            String logMessage = "Список телеграмм или список серверов баз данных пуст!<br/>" +
                    "Добавьте в список хотя бы одно значение или включите уже имеющееся.";
            logger.warn(logMessage);
            notificationPanel.addWarningNotification(logMessage);
            buttonPanel.enableStartButton();
            buttonPanel.disableStopButton();
            UserNotifier.errorMessage("Пустой список", logMessage);
            notificationPanel.addNotification("Работа прекращена.");
            throw ex;
        }
    }

    @Override
    public void run() {
        String logMessage = "Начало работы.";
        logger.info(logMessage);
        notificationPanel.addNotification(logMessage);
        buttonPanel.disableStartButton();
        buttonPanel.enableStopButton();

        for (Telegram telegram : enabledTelegrams) {
            for (DBServer dbServer : enabledDBServers) {
                scheduler.schedule(telegram.getCheckPattern(), new TelegramTask(telegram, dbServer));
            }
        }
        scheduler.start();
    }

    public void stop() {
        String logMessage = "Работа прекращена.";
        logger.info(logMessage);
        notificationPanel.addNotification(logMessage);
        buttonPanel.enableStartButton();
        buttonPanel.disableStopButton();

        scheduler.stop();
    }
}