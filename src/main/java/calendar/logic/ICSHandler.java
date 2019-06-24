package main.java.calendar.logic;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import main.java.calendar.data.CalendarEvent;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class ICSHandler {
    public static void exportDataToICS(CalendarEvent calendarEvent) {
        File file = null;
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("*.ics", "ics"));
        int returnVal = fc.showOpenDialog(new JFrame());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
        ICalendar ics = new ICalendar();
        VEvent event = new VEvent();

        System.out.println("File to write: " + calendarEvent);

        LocalDateTime localDateTime = LocalDateTime.of(
                calendarEvent.getCalendar().get(Calendar.YEAR),
                calendarEvent.getCalendar().get(Calendar.MONTH) + 1,
                calendarEvent.getCalendar().get(Calendar.DAY_OF_MONTH),
                calendarEvent.getCalendar().get(Calendar.HOUR_OF_DAY),
                calendarEvent.getCalendar().get(Calendar.MINUTE));
        Date eventTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        event.setDateStart(eventTime);
        event.setLocation(calendarEvent.getPlace());
        event.setDescription(calendarEvent.getDescription());

        ics.addEvent(event);

        try (FileWriter writer = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(Biweekly.write(ics).go());

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
