package com.mr_faton.gui.dialog;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.entity.DBServer;
import com.mr_faton.core.util.SettingsHolder;
import com.mr_faton.gui.frame.MainFrame;
import com.mr_faton.gui.notifier.UserNotifier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DBServersDialog extends JDialog{
    private static int WIDTH = 730;
    private static int HEIGHT = 250;
    private List<DBServer> dbServerList;

    public DBServersDialog() {
        super((MainFrame) AppContext.getBeanByName("mainFrame"), "Настройки серверов БД", true);

        dbServerList = SettingsHolder.getDbServerList();

        String[] columnHeaders = {"Имя", "URL", "Имя пользователя", "Пароль", "Включен"};

        final DefaultTableModel tableModel = new DefaultTableModel(0, 5);
        tableModel.setColumnIdentifiers(columnHeaders);

        if (dbServerList != null) {
            int i = 0;
            for (DBServer dbServer : dbServerList) {
                tableModel.insertRow(i, new Object[] {
                        dbServer.getName(),
                        dbServer.getUrl(),
                        dbServer.getUser(),
                        dbServer.getUserPassword(),
                        dbServer.getState()
                });
                i++;
            }
        }

        final JTable table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return String.class;
                    case 1: return String.class;
                    case 2: return String.class;
                    case 3: return String.class;
                    case 4: return Boolean.class;
                    default: return Object.class;
                }
            }
        };

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(125);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(135);
        columnModel.getColumn(3).setPreferredWidth(135);

        //make cursor visible in cell
        table.setSurrendersFocusOnKeystroke(true);

        table.setBackground(Color.white);

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
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cellValidator(tableModel)) return;

                List<DBServer> updatedDbServerList = new ArrayList<>();

                int rows = tableModel.getRowCount();
                for (int rowNumber = 0; rowNumber < rows; rowNumber++) {
                    DBServer dbServer = new DBServer();

                    dbServer.setName((String) tableModel.getValueAt(rowNumber, 0));
                    dbServer.setUrl((String) tableModel.getValueAt(rowNumber, 1));
                    dbServer.setUser((String) tableModel.getValueAt(rowNumber, 2));
                    dbServer.setUserPassword((String) tableModel.getValueAt(rowNumber, 3));
                    dbServer.setState((Boolean) tableModel.getValueAt(rowNumber, 4));

                    updatedDbServerList.add(dbServer);
                }

                SettingsHolder.updateDBServerList(updatedDbServerList);

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
}
