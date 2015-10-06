package com.mr_faton;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.gui.frame.MainFrame;
import org.apache.log4j.Logger;

public class StartGUI {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.StartGUI");
    public static void main(String[] args) {
        try {
            SettingsHolder.load();
        } catch (Exception e) {
            logger.warn("exception", e);
        }
        MainFrame mainFrame = (MainFrame) AppContext.getBeanByName("mainFrame");
        mainFrame.setVisible(true);
        mainFrame.toFront();
    }
}
