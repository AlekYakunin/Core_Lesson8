import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // Экземпляр класса OkHttpClient выполняет всю работу по созданию и отправке запросов
        OkHttpClient client = new OkHttpClient();

        // Город для прогноза
        //String city = "Sankt Peterburg";
        String city = "Санкт Петербург";

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("community-open-weather-map.p.rapidapi.com")
                .addPathSegment("forecast")
                .addQueryParameter("q", city)
                .addQueryParameter("units", "metric")
                .addQueryParameter("lang", "ru")
                .build();

        // Экземпляр класса Request создается через Builder
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "EWEIXXpcZYmsh8raBhA0iVVtKcrTp1o0FHojsnREuXf2IjMjEa")
                .build();

        Response response = null;
        String body = null;
        try {
            // Получение объекта ответа от сервера
            response = client.newCall(request).execute();

            // Тело сообщения возвращается методом body объекта Response
            body = response.body().string();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Ответ сервера
        System.out.println("Ответ сервера");
        System.out.println(response.code());

        if (body != null) {
            System.out.println(body+"\n");
            // класс для парсинга и печати JSON
            WeatherResponse weatherResponse = new WeatherResponse(body, city);

            System.out.println("Обработка JSON строки...");
            try {
                weatherResponse.responseWeather();
            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
            //Menu.printWeather(weatherResponse.getWeatherList(), city);

            ArrayList<WeatherData> weatherList;
            Menu menu = new Menu();

            // работа с базой данных (БД)
            Database db = new Database();
            // установим подключение к БД
            db.connect();
            try {
                System.out.print("Запись данных от сервера в БД ");
                // цикл по данным от сервера
                for (WeatherData data : weatherResponse.getWeatherList()) {
                    // запись данных от сервера в БД
                    db.insertUpdate(city,  data);
                    System.out.print(".");
                }
                System.out.println();

                // выведем меню для вариантов чтения из БД
                menu.display(db, city);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            // Закроем подключение к БД
            db.connectionClose();
        }

    }
}
