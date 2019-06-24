package main.java.calendar.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SoundFrame {
    private JLabel datePick;
    private JOptionPane optionPane;
    private JButton okButton, cancelButton;
    private ActionListener okEvent, cancelEvent;
    private JDialog dialog;
    private JCheckBox checkBox;

    public SoundFrame(boolean currentStatus){
        datePick = new JLabel("Sound status: ");
        checkBox = new JCheckBox();
        checkBox.setSelected(currentStatus);
        createAndDisplayOptionPane();
    }

    private void createAndDisplayOptionPane(){
        setupButtons();
        JPanel pane = layoutComponents();
        optionPane = new JOptionPane(pane);
        optionPane.setOptions(new Object[]{okButton, cancelButton});
        dialog = optionPane.createDialog("Select option");
        dialog.setTitle("SoundFrame");
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
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(datePick, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panel.add(checkBox, gridBagConstraints);
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

    public boolean getSelectedItem(){
        return checkBox.isSelected();
    }
}
