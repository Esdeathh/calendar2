package main.java.calendar.ui;

import main.java.calendar.data.CalendarEvent;
import main.java.calendar.logic.MainLogic;
import main.java.calendar.logic.MainLogicInterface;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

public class MainFrame extends JFrame implements MainFrameInterface, ActionListener {
    private JMenuBar menuBar;
    private JMenu menu, help, xmlFile, icsFile;
    private JMenuItem aboutProgram, deleteUnder, createEvent, xmlFileImport, xmlFileExport, icsExport, options;
    private CalendarFrame calendarFrame;

    MainLogicInterface mainLogicInterface = new MainLogic(this);

    public MainFrame(int width, int height) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Calendar");
        this.setSize(width, height);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        JPanel jPanel = new JPanel(new GridBagLayout());

        calendarFrame = new CalendarFrame(mainLogicInterface);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel.add(calendarFrame, gridBagConstraints);
        add(jPanel, BorderLayout.NORTH);
        setupMenu();

    }

    private void setupMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        deleteUnder = new JMenuItem("DeleteUnder");
        deleteUnder.addActionListener(this);
        deleteUnder.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        menu.add(deleteUnder);


        createEvent = new JMenuItem("Create event");
        createEvent.addActionListener(this);
        createEvent.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menu.add(createEvent);


        xmlFile = new JMenu("XML");
        menu.add(xmlFile);
        xmlFileExport = new JMenuItem("export");
        xmlFileExport.addActionListener(this);
        xmlFileExport.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        xmlFile.add(xmlFileExport);
        xmlFileImport = new JMenuItem("import");
        xmlFileImport.addActionListener(this);
        xmlFileImport.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        xmlFile.add(xmlFileImport);


        icsFile = new JMenu("ICS");
        menu.add(icsFile);
        icsExport = new JMenuItem("export");
        icsExport.addActionListener(this);
        icsExport.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        icsFile.add(icsExport);

        options = new JMenuItem("options");
        options.addActionListener(this);
        options.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menu.add(options);


        help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        menuBar.add(help);

        aboutProgram = new JMenuItem("About", KeyEvent.VK_I);
        aboutProgram.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        aboutProgram.addActionListener(this);
        help.add(aboutProgram);
        setJMenuBar(menuBar);
    }

    @Override
    public void showEvents(List<CalendarEvent> calendarEvents) {
        CalendarEventList dialog = new CalendarEventList("List of events: ", calendarEvents);
        dialog.show();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainLogicInterface.playSound();
        if (e.getSource() == aboutProgram) {
            JOptionPane.showConfirmDialog(this,
                    "Calendar\n" +
                            "Authors: \n" +
                            "211835 Lukasz Radosz \n" +
                            "211975 Marcin Rogalinski", "About", JOptionPane.DEFAULT_OPTION);
        } else if (e.getSource() == deleteUnder) {
            mainLogicInterface.handleDeleteUnder();
        } else if (e.getSource() == createEvent) {
            mainLogicInterface.handleAddEventOpened();
        } else if (e.getSource() == xmlFileImport) {
            final JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(new FileNameExtensionFilter("*.xml", "xml"));
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                mainLogicInterface.handleEventImportedFromXML(fc.getSelectedFile());
            }
        } else if (e.getSource() == xmlFileExport) {
            final JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(new FileNameExtensionFilter("*.xml", "xml"));
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                mainLogicInterface.handleEventExportedToXML(fc.getSelectedFile());
            }
        } else if (e.getSource() == icsExport) {
            mainLogicInterface.handleEventExportedToICS();
        } else if (e.getSource() == options) {
            mainLogicInterface.handleSettingsOpened();
        }
    }
}
