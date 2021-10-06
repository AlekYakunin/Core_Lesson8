import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    ArrayList<WeatherData> weatherList;

    public void display(Database db, String city) throws SQLException {

        Scanner input = new Scanner(System.in);
        boolean flagDayInput = false;

        display: while(true) {

            System.out.println("\n-- Чтение из БД --");
            if(!flagDayInput) {
                System.out.println(
                        "Выберите пункт: \n" +
                                "  1) Весь список БД\n" +
                                "  2) Данные за день октябрь 2021\n" +
                                "  3) Выход\n "
                );
            } else {
                System.out.println("Введите день");
            }
            int selection = input.nextInt();
            input.nextLine();

            switch (selection) {
                case 1:
                    if (!flagDayInput) {
                        selectDay(selection, db, city, "SELECT * FROM weather");
                    } else {
                        selectDay(selection, db, city, "SELECT * FROM weather WHERE LocalDate LIKE '%"+
                                setLeadingNull(selection)+".10.2021%'");
                        flagDayInput = false;
                    }
                    break;
                case 2:
                    if (!flagDayInput) {
                        flagDayInput = true;    // установим флаг для ввода даты (чтобы не срабатывало меню)
                    } else {
                        selectDay(selection, db, city, "SELECT * FROM weather WHERE LocalDate LIKE '%" +
                                setLeadingNull(selection) + ".10.2021%'");
                        flagDayInput = false;
                    }
                    break;
                case 3:
                    System.out.println("Выход...");
                    break display;
                default:
                    if (!flagDayInput) {
                        System.out.println("Неверный ввод!");
                    } else {
                        selectDay(selection, db, city, "SELECT * FROM weather WHERE LocalDate LIKE '%" +
                                setLeadingNull(selection) + ".10.2021%'");
                        flagDayInput = false;
                    }
                    break;
            }
        }
    }

    private void selectDay(int selection, Database db, String city, String sqlStr) throws SQLException {
        if (weatherList != null)  weatherList.clear();

        if (selection > 0 && selection <= 31) {
            if (weatherList != null)  weatherList.clear();
            weatherList = db.readData(sqlStr);
            printWeather(weatherList, city);
        }

    }

    public void printWeather(ArrayList<WeatherData> weatherList, String city) {

        for (WeatherData weatherData : weatherList) {
            System.out.print("В городе " + city + "  на дату ");
            System.out.print(weatherData.getLocalDate() + "  ожидается:  ");
            System.out.print(String.format("  Температура: %1$5s",  weatherData.getTemperature()));   // Температура
            System.out.print("  Влажность: " + weatherData.getHumidity());  // Влажность
            System.out.print("  Давление: " + weatherData.getPressure());   // Давление
            System.out.println(" " + weatherData.getWeatherText());         // описание
        }
    }

    private String setLeadingNull(int selection) {
        if(selection < 10 ) return  "0" + selection;
        else return Integer.toString(selection);
    }
}
