package main.java.calendar.logic;

import main.java.calendar.data.CalendarEvent;
import main.java.calendar.data.SQLDataBase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class XMLHandler {
    public static void exportDataToXML(File file) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        Element root = document.createElement("events");
        document.appendChild(root);

        for (CalendarEvent calendarEvent : SQLDataBase.getInstance().getAllEvents()) {
            Element event = document.createElement("event");
            root.appendChild(event);

            Element calendar = document.createElement("calendar");
            event.appendChild(calendar);

            Element year = document.createElement("year");
            calendar.appendChild(year);
            year.appendChild(document.createTextNode(String.valueOf(calendarEvent.getCalendar().get(Calendar.YEAR))));
            Element month = document.createElement("month");
            calendar.appendChild(month);
            month.appendChild(document.createTextNode(String.valueOf(calendarEvent.getCalendar().get(Calendar.MONTH)+1)));
            Element day = document.createElement("day");
            calendar.appendChild(day);
            day.appendChild(document.createTextNode(String.valueOf(calendarEvent.getCalendar().get(Calendar.DAY_OF_MONTH))));
            Element hour = document.createElement("hour");
            calendar.appendChild(hour);
            hour.appendChild(document.createTextNode(String.valueOf(calendarEvent.getCalendar().get(Calendar.HOUR_OF_DAY))));
            Element minute = document.createElement("minute");
            calendar.appendChild(minute);
            minute.appendChild(document.createTextNode(String.valueOf(calendarEvent.getCalendar().get(Calendar.MINUTE))));

            Element description = document.createElement("description");
            event.appendChild(description);
            description.appendChild(document.createTextNode(calendarEvent.getDescription()));

            Element place = document.createElement("place");
            event.appendChild(place);
            place.appendChild(document.createTextNode(calendarEvent.getPlace()));
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(file);

        transformer.transform(domSource, streamResult);

        System.out.println("Done creating XML File");
    }

    public static void importDataFromXML(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        for (int i = 0; i < document.getElementsByTagName("event").getLength(); i++) {
            Element e = (Element) document.getElementsByTagName("event").item(i);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(e.getElementsByTagName("year").item(0).getTextContent()),Integer.valueOf(e.getElementsByTagName("month").item(0).getTextContent())-1,Integer.valueOf(e.getElementsByTagName("day").item(0).getTextContent()),Integer.valueOf(e.getElementsByTagName("hour").item(0).getTextContent()),Integer.valueOf(e.getElementsByTagName("minute").item(0).getTextContent()));
            CalendarEvent calendarEvent = new CalendarEvent(calendar, e.getElementsByTagName("description").item(0).getTextContent(),e.getElementsByTagName("place").item(0).getTextContent(), 0);
            System.out.println(calendarEvent);
            SQLDataBase.getInstance().addEvent(calendarEvent);
        }
    }
}
