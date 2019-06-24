package main.java.calendar.logic;

import java.io.File;
import java.util.Calendar;

public interface MainLogicInterface {

    void handleEventExportedToXML(File file);

    void handleEventImportedFromXML(File file);

    void handleEventExportedToICS();

    void handleAddEventOpened();

    void handleSettingsOpened();

    void handleDeleteUnder();

    void checkEventsInSpecificDay(Calendar calendar);

    void playSound();

    void checkEventsInMinute(int minute);


}
