package main.java.calendar;

import main.java.calendar.ui.MainFrame;

import javax.swing.*;

public class Calendar {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        MainFrame mainFrame = new MainFrame(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
        mainFrame.setVisible(true);
    }
}
