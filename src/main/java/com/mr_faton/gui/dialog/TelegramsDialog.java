package com.mr_faton.gui.dialog;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.entity.Telegram;
import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.gui.frame.MainFrame;
import com.mr_faton.gui.notifier.UserNotifier;
import it.sauronsoftware.cron4j.InvalidPatternException;
import it.sauronsoftware.cron4j.SchedulingPattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class TelegramsDialog extends JDialog {
    private static int WIDTH = 810;
    private static int HEIGHT = 280;
    private List<Telegram> telegramList;

    public TelegramsDialog() {
        super((MainFrame) AppContext.getBeanByName("mainFrame"), "Настройки телеграмм", true);

        telegramList = SettingsHolder.getTelegramList();

        String[] columnHeaders = {
                "Заголовок",
                "Цифр заголовок",
                "Глубина поиска в мин",
                "Шаблон проверки",
                "Включен"};
        final DefaultTableModel tableModel = new DefaultTableModel(0, 5);
        tableModel.setColumnIdentifiers(columnHeaders);

        if (telegramList != null) {
            int i = 0;
            for (Telegram telegram : telegramList) {
                tableModel.insertRow(i, new Object[] {
                        telegram.getHeader(),
                        telegram.getDigitalHeader(),
                        telegram.getSearchDepth(),
                        telegram.getCheckPattern(),
                        telegram.getState()
                });
                i++;
            }
        }

        final JTable table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return String.class;
                    case 1: return String .class;
                    case 2: return Integer.class;
                    case 3: return String .class;
                    case 4: return Boolean.class;
                    default: return Object.class;
                }
            }
        };

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(250);
        columnModel.getColumn(4).setPreferredWidth(65);

        //make cursor visible in cell
        table.setSurrendersFocusOnKeystroke(true);

        //устанавливаем цвет фона ячеек белым
        table.setBackground(Color.white);
        //добавляем скроллер в таблицу (так же это позволяет сделать видимыми название столбцов таблицы)
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4, 5, 5));

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //добавляем пустую строку в таблицу
                tableModel.addRow(new String[]{});
                int lastRow = tableModel.getRowCount() - 1;
                int lastColumn = tableModel.getColumnCount() - 1;
                tableModel.setValueAt(false, lastRow, lastColumn);
            }
        });

        JButton removeButton = new JButton("Удалить");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    tableModel.removeRow(row);
                } else {
                    UserNotifier.warningMessage("Строка не выбрана!", "Строка для удаления не выбрана!\n" +
                            "Пожалуйста выберите строку, а потом удаляйте!");
                }
            }
        });

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(new ActionListener() {
            //сохранить всю таблиыу в настройки
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cellValidator(tableModel) || !patternValidator(tableModel)) return;

                List<Telegram> updatedTelegramList = new ArrayList<>();

                int rows = tableModel.getRowCount();
                for (int rowNumber = 0; rowNumber < rows; rowNumber++) {
                    Telegram telegram = new Telegram();

                    telegram.setHeader((String) tableModel.getValueAt(rowNumber, 0));
                    telegram.setDigitalHeader((String) tableModel.getValueAt(rowNumber, 1));
                    telegram.setSearchDepth((Integer) tableModel.getValueAt(rowNumber, 2));
                    telegram.setCheckPattern((String) tableModel.getValueAt(rowNumber, 3));
                    telegram.setState((Boolean) tableModel.getValueAt(rowNumber, 4));

                    updatedTelegramList.add(telegram);
                }

                SettingsHolder.updateTelegramList(updatedTelegramList);

                UserNotifier.infoMessage("Сохранение", "Настройки сохранены успешно");
            }
        });

        JButton cancelButton = new JButton("Закрыть");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //просто делает окно настроек шаблоно невидимым
                setVisible(false);
                dispose();
            }
        });

        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        setLayout(new BorderLayout(5, 5));

        add(scrollPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension monitorScreenSize = toolkit.getScreenSize();
        int monitorWidth = monitorScreenSize.width;
        int monitorHeight = monitorScreenSize.height;
        //устанавливаем размер
        setSize(WIDTH, HEIGHT);
        //устанавливаем положение (координаты фрейма)
        setLocation(monitorWidth / 2 - WIDTH / 2, monitorHeight / 2 - HEIGHT / 2);
    }

    private boolean cellValidator(final TableModel tableModel) {
        int rows = tableModel.getRowCount();
        int columns = tableModel.getColumnCount();

        for (int curRow = 0; curRow < rows; curRow++) {
            for (int curColumn = 0; curColumn < columns; curColumn++) {
                Object cellValue = tableModel.getValueAt(curRow, curColumn);
                if (cellValue == null || cellValue.toString().length() == 0) {
                    UserNotifier.warningMessage("Пустая ячейка",
                            "Ячейка в строке №" + (++curRow) + " и столбце №" + (++curColumn) + " не заполнена.<br/>" +
                                    "Заполните ячейку или снимите с неё выделение.");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean patternValidator(final TableModel tableModel) {
        int patternColumn = 3;
        int rows = tableModel.getRowCount();
        for (int curRow = 0; curRow < rows; curRow++) {
            if (!SchedulingPattern.validate((String) tableModel.getValueAt(curRow, patternColumn))) {
                UserNotifier.warningMessage("Неправильный шаблон",
                        "Вы ввели неверный шаблон для проверки в строке №" + (++curRow) + ".<br/>" +
                                "Пожалуйста, исправте этот шаблон.");
                return false;
            }
        }
        return true;
    }
}
