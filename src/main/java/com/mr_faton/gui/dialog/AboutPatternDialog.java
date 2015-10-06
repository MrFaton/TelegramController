package com.mr_faton.gui.dialog;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.gui.frame.MainFrame;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 06.10.2015
 */
public class AboutPatternDialog extends JDialog {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.gui.dialog.AboutPatternDialog");
    private static int WIDTH = 1100;
    private static int HEIGHT = 550;

    public AboutPatternDialog() {
        super((MainFrame) AppContext.getBeanByName("mainFrame"), "Описание шаблонов", false);

        ClassLoader classLoader = getClass().getClassLoader();
        URL fileURL = classLoader.getResource("AboutPatterns.html");
        System.out.println(fileURL.getPath());
        JEditorPane content = null;
        try {
            content = new JEditorPane(fileURL);
            content.setEditable(false);
        } catch (IOException e) {
            logger.warn("exception during open AboutPatternDialog", e);
        }


        JScrollPane scroller = new JScrollPane(content);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //контент помещается прямо во внутрь фрейма, без панелей
        add(scroller);

        //получаем разрешение монитора для того, чтобы выводить наш фрейм по центру
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension monitorScreenSize = toolkit.getScreenSize();
        int monitorWidth = monitorScreenSize.width;
        int monitorHeight = monitorScreenSize.height;
        //устанавливаем размер
        setSize(WIDTH, HEIGHT);
        //устанавливаем положение (координаты фрейма)
        setLocation(monitorWidth / 2 - WIDTH / 2, monitorHeight / 2 - HEIGHT / 2);
        //делаем чтобы окно не могло менять размер
        setResizable(true);
    }
}
