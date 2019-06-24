package main.java.calendar.ui;

import main.java.calendar.data.CalendarEvent;
import main.java.calendar.exceptions.InvalidCalendarEventException;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class EventCreator {
    private JLabel label, place, description, h, mi, datePick;
    private JOptionPane optionPane;
    private JTextArea tPlace, tDescription;
    private JButton okButton, cancelButton;
    private JComboBox<String> hour, mins;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;
    private JDatePicker datePicker;

    public EventCreator(){
        label = new JLabel("Create event");
        datePick = new JLabel("Pick date: ");
        h = new JLabel("Hour:");
        mi = new JLabel("Minute:");
        place = new JLabel("Place:");
        description = new JLabel("Description:");
        datePicker = new JDatePicker(Calendar.getInstance());
        tPlace = new JTextArea();
        tDescription = new JTextArea();
        hour = new JComboBox<>();
        mins = new JComboBox<>();
        for (int i = 0; i <= 23; i++) {
            hour.addItem(String.valueOf(i));
        }
        for (int i = 0; i <= 59; i++) {
            mins.addItem(String.valueOf(i));
        }
        createAndDisplayOptionPane();
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
        dialog.setTitle("EventCreator");
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

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panel.add(h, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panel.add(hour, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panel.add(mi, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panel.add(mins, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panel.add(place, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panel.add(tPlace, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panel.add(description, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panel.add(tDescription, gridBagConstraints);
        return panel;
    }

    public void setOnOk(ActionListener event){ okEvent = event; }

    public void setOnClose(ActionListener event){
        cancelEvent  = event;
    }

    private void handleOkButtonClick(ActionEvent e){
        if (tDescription.getText().isEmpty()) throw new InvalidCalendarEventException();
        if (tPlace.getText().isEmpty()) throw new InvalidCalendarEventException();
        if(okEvent != null){ okEvent.actionPerformed(e); }
        hide();
    }

    private void handleCancelButtonClick(ActionEvent e){
        if(cancelEvent != null){ cancelEvent.actionPerformed(e);}
        hide();
    }

    public void show(){ dialog.setVisible(true); }

    private void hide(){ dialog.setVisible(false); }

    public CalendarEvent getSelectedItem(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getModel().getYear(), datePicker.getModel().getMonth(), datePicker.getModel().getDay(), Integer.valueOf((String)hour.getSelectedItem()),Integer.valueOf((String)mins.getSelectedItem()));
        return new CalendarEvent(calendar, tDescription.getText(), tPlace.getText(), 0);
    }
}
