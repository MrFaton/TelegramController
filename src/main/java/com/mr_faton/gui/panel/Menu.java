package com.mr_faton.gui.panel;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.gui.dialog.AboutDialog;
import com.mr_faton.gui.dialog.AboutPatternDialog;
import com.mr_faton.gui.dialog.DBServersDialog;
import com.mr_faton.gui.dialog.TelegramsDialog;
import com.mr_faton.gui.frame.MainFrame;
import com.mr_faton.gui.notifier.UserNotifier;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Menu extends JMenuBar {

    public Menu(final ButtonPanel buttonPanel) {

        JMenu settings = new JMenu("Настройки");
        JMenu help = new JMenu("Помощь");

        JMenuItem telegramSettings = new JMenuItem("Телеграммы");
        JMenuItem dbServerSettings = new JMenuItem("Серверы БД");
        JMenuItem alarmSoundSettings = new JMenuItem("Звук");

        final JMenuItem aboutPattern = new JMenuItem("Шаблоны");
        final JMenuItem about = new JMenuItem("О программе");

        telegramSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!buttonPanel.isStartButtonEnabled()) {
                    UserNotifier.warningMessage("Просмотр и редактирование настроек",
                            "Во время работы программы просмотр и редактирование настроек недоступен.<p/>" +
                                    "Пожалуйста остановите работу приложения, а занем просматривайте и редактируйте " +
                                    "настройки телеграмм.");
                    return;
                }

                TelegramsDialog telegramsDialog = new TelegramsDialog();
                telegramsDialog.setVisible(true);
                telegramsDialog.toFront();
            }
        });

        dbServerSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!buttonPanel.isStartButtonEnabled()) {
                    UserNotifier.warningMessage("Просмотр и редактирование настроек",
                            "Во время работы программы просмотр и редактирование настроек недоступен.<p/>" +
                                    "Пожалуйста остановите работу приложения, а занем просматривайте и редактируйте " +
                                    "настройки серверов баз данных.");
                    return;
                }

                DBServersDialog dbServersDialog = new DBServersDialog();
                dbServersDialog.setVisible(true);
                dbServersDialog.toFront();
            }
        });

        aboutPattern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutPatternDialog aboutPatternDialog = new AboutPatternDialog();
                aboutPatternDialog.setVisible(true);
                aboutPatternDialog.toFront();
            }
        });

        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog aboutDialog = new AboutDialog();
                aboutDialog.setVisible(true);
                aboutDialog.toFront();
            }
        });

        alarmSoundSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!buttonPanel.isStartButtonEnabled()) {
                    UserNotifier.warningMessage("Просмотр и редактирование настроек",
                            "Во время работы программы просмотр и редактирование настроек недоступен.<p/>" +
                                    "Пожалуйста остановите работу приложения, а занем просматривайте и редактируйте " +
                                    "настройки звукового оповещения.");
                    return;
                }

                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                FileFilter filter = new FileNameExtensionFilter("WAV files", "wav");
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setSelectedFile(new File(SettingsHolder.getAlarmSoundPath()));
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showDialog((MainFrame) AppContext.getBeanByName("mainFrame"), "Select");
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    SettingsHolder.saveAlarmSoundPath(selectedFile.getAbsolutePath());
                }
            }
        });

        settings.add(telegramSettings);
        settings.add(dbServerSettings);
        settings.add(alarmSoundSettings);

        help.add(aboutPattern);
        help.add(about);

        add(settings);
        add(help);
    }
}
