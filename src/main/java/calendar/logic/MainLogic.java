package main.java.calendar.logic;

import main.java.calendar.data.CalendarEvent;
import main.java.calendar.data.OptionMemory;
import main.java.calendar.data.SQLDataBase;
import main.java.calendar.ui.*;
import org.xml.sax.SAXException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class MainLogic implements MainLogicInterface {
    MainFrameInterface mainFrameInterface;
    public  MainLogic(MainFrameInterface mainFrameInterface) {
        this.mainFrameInterface = mainFrameInterface;

        Runnable runnable = () -> {
            for (;;) {
                checkEventsInMinute(30);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    @Override
    public void handleEventExportedToXML(File file) {
        try {
            XMLHandler.exportDataToXML(file);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEventImportedFromXML(File file) {
        try {
            XMLHandler.importDataFromXML(file);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEventExportedToICS() {
        CalendarEventList calendarEventList = new CalendarEventList("List of events: ", SQLDataBase.getInstance().getAllEvents());
        calendarEventList.setOnOk(x -> ICSHandler.exportDataToICS(calendarEventList.getSelectedItem()));
        calendarEventList.show();
    }

    @Override
    public void handleAddEventOpened() {
        EventCreator eventCreator = new EventCreator();
        eventCreator.setOnOk(x -> SQLDataBase.getInstance().addEvent(eventCreator.getSelectedItem()));
        eventCreator.show();
    }

    @Override
    public void handleSettingsOpened() {
        SoundFrame soundFrame = new SoundFrame(OptionMemory.getInstance().getSoundStatus());
        soundFrame.setOnOk(x -> OptionMemory.getInstance().setSoundStatus(soundFrame.getSelectedItem()));
        soundFrame.show();
    }

    @Override
    public void handleDeleteUnder() {
        DeleteUnderFrame deleteUnderFrame = new DeleteUnderFrame();
        deleteUnderFrame.setOnOk(e -> SQLDataBase.getInstance().deleteOlderThan(deleteUnderFrame.getSelectedItem()));
        deleteUnderFrame.show();
    }

    @Override
    public void checkEventsInSpecificDay(Calendar calendar) {
        CalendarEventList calendarEventList = new CalendarEventList("List of events: ", SQLDataBase.getInstance().getEventsOnSpecificDate(calendar));
        calendarEventList.show();
    }

    @Override
    public void playSound() {
        if (OptionMemory.getInstance().getSoundStatus()) {
            try {
                String soundName = "src/main/java/calendar/Pop-sound-effect.wav";
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void checkEventsInMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        List<CalendarEvent> calendarEvents = SQLDataBase.getInstance().getEventsOnSpecificDate(calendar);
        calendarEvents = calendarEvents.stream().filter(s ->
                (((s.getCalendar().get(Calendar.HOUR_OF_DAY))*60)+s.getCalendar().get(Calendar.MINUTE)) - (((calendar.get(Calendar.HOUR_OF_DAY))*60)+calendar.get(Calendar.MINUTE)) <= minute &&
                (((s.getCalendar().get(Calendar.HOUR_OF_DAY))*60)+s.getCalendar().get(Calendar.MINUTE)) - (((calendar.get(Calendar.HOUR_OF_DAY))*60)+calendar.get(Calendar.MINUTE)) >= 0)
                .collect(Collectors.toList());
        if (!calendarEvents.isEmpty())mainFrameInterface.showEvents(calendarEvents);
    }
}
