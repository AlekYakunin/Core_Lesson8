import java.sql.*;
import java.util.ArrayList;

public class Database {

    private Connection conn = null;
    private Statement stmt;

    public void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/sqlite/weather.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Подключение к SQLite установлено.");

            conn.setAutoCommit(false);
            stmt = conn.createStatement();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertUpdate(String city, WeatherData data) throws SQLException {
        // Вставка строки
        String sqlStr = String.format("INSERT INTO weather (City, LocalDate, WeatherText, Temperature, Humidity, Pressure) "+
                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s') ",
                city, data.getLocalDate(), data.getWeatherText(), data.getTemperature(), data.getHumidity(), data.getPressure());

        // или обновление, если строка с такой датой и временем существует - ON CONFLICT(LocalDate)
        String sqlStr2 = String.format(" ON CONFLICT(LocalDate) DO UPDATE SET City = '%s', LocalDate = '%s', "+
                        "WeatherText = '%s', Temperature = '%s', Humidity = '%s', Pressure = '%s';",
                city, data.getLocalDate(), data.getWeatherText(), data.getTemperature(), data.getHumidity(), data.getPressure());

        int result = stmt.executeUpdate(sqlStr + sqlStr2);
    }

    public ArrayList<WeatherData> readData(String sqlStr) throws SQLException {
        WeatherData data;
        ArrayList<WeatherData> weatherList;
        weatherList = new ArrayList<WeatherData>();

        // выбор всех данных
        ResultSet rs = stmt.executeQuery(sqlStr);

        while (rs.next()) { // Пока есть строки

            data = new WeatherData(rs.getString("LocalDate"),
                    rs.getString("WeatherText"),
                    Double.parseDouble(rs.getString("Temperature")),
                    rs.getString("Humidity"),
                    rs.getString("Pressure"));
            weatherList.add(data);
        }
        return weatherList;
    }

    public void connectionClose() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
