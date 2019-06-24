package main.java.calendar.data;

import main.java.calendar.exceptions.InvalidCalendarEventException;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLDataBase {

    private static final SQLDataBase instance = new SQLDataBase();

    private static Connection connection = null;

    private SQLDataBase() {
        checkIfTableExistAndCreate();
    }

    private void checkIfTableExistAndCreate() {
        connect();
        String createTable = "CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY AUTOINCREMENT, year int, month int, day int, hour int, minute int, event_description varchar(255), event_place varchar(255))";
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SQLDataBase getInstance() {
        return instance;
    }

    public void connect() {
        final String databaseName = "database";
        final String resourcesDirectoryPath = Paths.get("src/main/resources/").toAbsolutePath().toString();

        try {
            // db parameters
            String url = "jdbc:sqlite:".concat(resourcesDirectoryPath).concat("/").concat(databaseName);
            // create a connection to the database
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            System.out.println("Connection closed");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEvent(CalendarEvent calendarEvent) {
        connect();
        String sql = "INSERT INTO events(year, month, day, hour, minute, event_description, event_place) VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(calendarEvent.getCalendar().get(Calendar.YEAR)));
            preparedStatement.setString(2, String.valueOf(calendarEvent.getCalendar().get(Calendar.MONTH)+1));
            preparedStatement.setString(3, String.valueOf(calendarEvent.getCalendar().get(Calendar.DAY_OF_MONTH)));
            preparedStatement.setString(4, String.valueOf(calendarEvent.getCalendar().get(Calendar.HOUR_OF_DAY)));
            preparedStatement.setString(5, String.valueOf(calendarEvent.getCalendar().get(Calendar.MINUTE)));
            preparedStatement.setString(6, calendarEvent.getDescription());
            preparedStatement.setString(7, calendarEvent.getPlace());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public List<CalendarEvent> getAllEvents() {
        connect();
        String sql = "SELECT id, year, month, day, hour, minute, event_description, event_place FROM events";

        List<CalendarEvent> calendarEvents = new ArrayList<>();

        try (Connection connection = SQLDataBase.connection;
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(
                            Integer.parseInt(resultSet.getString("year")),
                            Integer.parseInt(resultSet.getString("month"))-1,
                            Integer.parseInt(resultSet.getString("day")),
                            Integer.parseInt(resultSet.getString("hour")),
                            Integer.parseInt(resultSet.getString("minute")));
                    CalendarEvent calendarEvent = new CalendarEvent(calendar,
                            resultSet.getString("event_description"), resultSet.getString("event_place"), Integer.parseInt(resultSet.getString("id")));
                    calendarEvents.add(calendarEvent);
                } catch (InvalidCalendarEventException e) {
                    System.out.println(e);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        return calendarEvents;
    }

    public void deleteOlderThan(Calendar calendar) {
        List<CalendarEvent> calendarEvents = getAllEvents();

        String sql = "DELETE FROM events WHERE id = ?";
        connect();

        try (Connection conn = connection; PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            calendarEvents.forEach(e -> {
                if (e.getCalendar().before(calendar)) {
                    try {
                        preparedStatement.setInt(1, e.getId());
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        closeConnection();
    }

    public List<CalendarEvent> getEventsOnSpecificDate(Calendar calendarTime) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        String sql = "SELECT * FROM events where (day LIKE ? AND month LIKE ? AND year LIKE ?)";
        connect();

        try (Connection conn = connection;
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, String.valueOf(calendarTime.get(Calendar.DAY_OF_MONTH)));
            preparedStatement.setString(2, String.valueOf(calendarTime.get(Calendar.MONTH) + 1));
            preparedStatement.setString(3, String.valueOf(calendarTime.get(Calendar.YEAR)));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(
                            Integer.parseInt(resultSet.getString("year")),
                            Integer.parseInt(resultSet.getString("month"))-1,
                            Integer.parseInt(resultSet.getString("day")),
                            Integer.parseInt(resultSet.getString("hour")),
                            Integer.parseInt(resultSet.getString("minute")));
                    CalendarEvent calendarEvent = new CalendarEvent(calendar,
                            resultSet.getString("event_description"), resultSet.getString("event_place"), Integer.parseInt(resultSet.getString("id")));
                    calendarEvents.add(calendarEvent);
                } catch (InvalidCalendarEventException e) {
                    System.out.println(e);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        return calendarEvents;
    }
}
