package com.mr_faton.gui.frame;

import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.gui.notifier.UserNotifier;
import com.mr_faton.gui.panel.ButtonPanel;
import com.mr_faton.gui.panel.Menu;
import com.mr_faton.gui.panel.NotificationPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class MainFrame extends JFrame {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.gui.frame.MainFrame");
    private final int WIDTH = 780;
    private final int HEIGHT = 360;

    public MainFrame(final NotificationPanel notificationPanel, final ButtonPanel buttonPanel, Menu menuBar) throws HeadlessException {
        setLayout(new BorderLayout(10, 10));
        setJMenuBar(menuBar);
        add(notificationPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension monitorScreenSize = toolkit.getScreenSize();
        int monitorWidth = monitorScreenSize.width;
        int monitorHeight = monitorScreenSize.height;

        setSize(WIDTH, HEIGHT);
        setLocation(monitorWidth / 2 - WIDTH / 2, monitorHeight / 2 - HEIGHT / 2);
        setResizable(true);
        setTitle("Telegram Controller");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        URL iconURL = getClass().getClassLoader().getResource("icon.png");
        Image icon = new ImageIcon(iconURL).getImage();
        setIconImage(icon);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!buttonPanel.isStartButtonEnabled()) {
                    UserNotifier.warningMessage("Предупреждение о закрытии!",
                            "Нельзя просто так взять и закрыть программу!<br/>" +
                                    "Сначала остановите её нажатием кнопки \"Стоп\".");
                    return;
                }

                setVisible(false);
                try {
                    SettingsHolder.save();
                } catch (Exception ex) {
                    logger.warn("exception while saving telegram or db list", ex);
                }

                dispose();
            }
        });
    }
}
