package main.java.calendar.ui;

import main.java.calendar.data.CalendarEvent;
import main.java.calendar.exceptions.InvalidCalendarEventException;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class DeleteUnderFrame {
    private JLabel label, datePick;
    private JOptionPane optionPane;
    private JButton okButton, cancelButton;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;
    private JDatePicker datePicker;

    public DeleteUnderFrame(){
        label = new JLabel("Delete event under");
        datePick = new JLabel("Pick date: ");
        datePicker = new JDatePicker(Calendar.getInstance());
        createAndDisplayOptionPane();
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
        dialog.setTitle("DeleteUnderFrame");
    }

    private void setupButtons(){
        okButton = new JButton("Ok");
        okButton.addActionListener(e -> handleOkButtonClick(e));

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancelButtonClick(e));
    }

    private JPanel layoutComponents(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(label, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel.add(datePick, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panel.add(datePicker, gridBagConstraints);
        return panel;
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if(okEvent != null){ okEvent.actionPerformed(e); }
        hide();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        hide();
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }

    public Calendar getSelectedItem(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getModel().getYear(), datePicker.getModel().getMonth(), datePicker.getModel().getDay());
        return calendar;
    }
}
