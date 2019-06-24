package main.java.calendar.ui;

import main.java.calendar.logic.MainLogic;
import main.java.calendar.logic.MainLogicInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Calendar;

public class CalendarFrame extends Container {
    JTable table;
    JLabel monthLabel;
    JButton previousButton, nextButton;
    LocalDate localDate;
    MainLogicInterface mainLogic;

    public CalendarFrame(MainLogicInterface mainLogicInterface) {
        this.setLayout(new GridBagLayout());
        this.setVisible(true);
        mainLogic = mainLogicInterface;

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        localDate = LocalDate.now();
        table = new JTable();
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});
        model.addColumn("cos", new Object[] {"", "", "", "", "", ""});

        table.setModel(model);
        table.setRowHeight(252/6);
        table.setDefaultEditor(Object.class, null);
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        monthLabel = new JLabel();
        updateCalendar();

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(previousButton, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(monthLabel, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        add(nextButton, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(table, gridBagConstraints);
        actionsInit();
    }

    private void updateCalendar() {
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int y = 0; y < table.getColumnCount(); y++) {
                table.setValueAt("", i,y);
            }
        }
        int tmp = 1;
        int tmp2 = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1).getDayOfWeek().getValue()-1;
        int row = 0;
        do {
            table.setValueAt(tmp,row,tmp2%7);
            if(tmp2%7 == 6) row++;
            tmp++;
            tmp2++;
        } while(tmp <= localDate.lengthOfMonth());
        monthLabel.setText(localDate.getMonth().name() + ", " + localDate.getYear());
    }

    private void actionsInit() {
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localDate = localDate.plusMonths(1);
                updateCalendar();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localDate = localDate.minusMonths(1);
                updateCalendar();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row=table.rowAtPoint(e.getPoint());
                int col= table.columnAtPoint(e.getPoint());
                Calendar calendar = Calendar.getInstance();
                calendar.set(localDate.getYear(),localDate.getMonthValue()-1,Integer.valueOf(table.getValueAt(row,col).toString()));
                mainLogic.checkEventsInSpecificDay(calendar);
            }
        });
    }
}
