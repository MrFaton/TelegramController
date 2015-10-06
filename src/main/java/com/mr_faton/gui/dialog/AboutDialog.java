package com.mr_faton.gui.dialog;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.gui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog{
    private static int WIDTH = 471;
    private static int HEIGHT = 256;

    public AboutDialog() {
        /*
        взываем конструктор предка (JDialog), передаём ему родительсткий фрейм, заголовок фрейма и признак модальности
        (т.е. когда это окно открыто и если "true", то пользователь не может работать с остальными окнами, пока не
        закроется это окно)
         */
        super((MainFrame) AppContext.getBeanByName("mainFrame"), "О программе", true);

        String text = "<html><h1 style=\"text-align: center;\"><font color=\"#0000FF\">Telegram Controller by Mr_Faton</font></h1>\n" +
                "<div align=\"center\"><font size=\"3\">(Контроль своевременности прохода телеграмм)</font></div>\n" +
                "<hr>\n" +
                "<ul>\n" +
                "<li><font size=\"4\">Автор: Понарин Игорь Сергеевич</font></li>\n" +
                "<li><font size=\"4\">Версия: 3.0</font></li>\n" +
                "<li><font size=\"4\">Разработанно для АМСГ Харьков Аэропорт</font></li>\n" +
                "</ul>\n" +
                "<hr>\n" +
                "<p><font size=\"3\">По всем вопросам обращайтесь по электронному адресу: <font color=\"#0000FF\">firefly90@inbox.ru</font></font></p>\n" +
                "<p align=\"right\">&nbsp;</p>\n" +
                "<p align=\"right\"><font size=\"2\">Copyright &copy; 2015 Понарин И.С.</font></p>\n</html>";
        //весь текст хранитья внутри объекта JLabel
        JLabel content = new JLabel(text);
        //контент помещается прямо во внутрь фрейма, без панелей
        add(content);

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
        setResizable(false);
    }
}
