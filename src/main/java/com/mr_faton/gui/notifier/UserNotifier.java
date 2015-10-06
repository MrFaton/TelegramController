package com.mr_faton.gui.notifier;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.gui.frame.MainFrame;

import javax.swing.*;

public class UserNotifier {
    private static final MainFrame mainFrame = (MainFrame) AppContext.getBeanByName("mainFrame");

    public static void warningMessage(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame,
                new JLabel("<html><body><div align=\"center\">" + message + "</div></body></html>", JLabel.CENTER),
                title,
                JOptionPane.WARNING_MESSAGE);
    }

    public static void infoMessage(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame,
                new JLabel("<html><body><div align=\"center\">" + message + "</div></body></html>", JLabel.CENTER),
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void errorMessage(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame,
                new JLabel("<html><body><div align=\"center\">" + message + "</div></body></html>", JLabel.CENTER),
                title,
                JOptionPane.ERROR_MESSAGE);
    }
}
