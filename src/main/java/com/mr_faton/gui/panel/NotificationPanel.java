package com.mr_faton.gui.panel;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.Date;

public class NotificationPanel extends JPanel {

    private final JTextArea notificationArea;

    public NotificationPanel() {
        notificationArea = new JTextArea();
        notificationArea.setLineWrap(true);
        notificationArea.setEditable(false);
        Font font = new Font("Verdana", Font.PLAIN, 14);
        notificationArea.setFont(font);
        DefaultCaret caret = (DefaultCaret) notificationArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
//        notificationArea.setForeground(Color.BLUE);

        JScrollPane scrollPane = new JScrollPane(notificationArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        setBorder(BorderFactory.createTitledBorder("Активность..."));
        setLayout(new BorderLayout(5, 5));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addNotification(String notification) {
        Date date = new Date(System.currentTimeMillis());
        notificationArea.append(String.format("%td-%<tm-%<tY %<tH:%<tM:%<tS", date) + " INFO " + notification + "\n");
    }

    public void addWarningNotification(String warningNotification) {
        Date date = new Date(System.currentTimeMillis());
        notificationArea.append(String.format("%td-%<tm-%<tY %<tH:%<tM:%<tS", date) + " WARN " + warningNotification + "\n");
    }
}
