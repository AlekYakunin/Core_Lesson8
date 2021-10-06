import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {

    private String stringJSON;
    private  String city;
    private ArrayList<WeatherData> weatherList;

    public WeatherResponse(String stringJSON, String city) {
        this.stringJSON = stringJSON;
        this.city = city;
        weatherList = new ArrayList<WeatherData>();
    }

    public ArrayList<WeatherData> getWeatherList() { return weatherList; }
    public void clearList() { weatherList.clear(); }

    public void responseWeather() throws JSONException {
        JSONObject obj = new JSONObject(stringJSON);
        JSONArray arr = obj.getJSONArray("list");       // выбор массива json по ключу "list"

        for (int i = 0; i < arr.length(); i++)              // цикл по массиву
        {
            JSONObject day = arr.getJSONObject(i);          // блок погоды за день

            String timeStamp =  day.get("dt").toString();   // дата unix
            java.util.Date dateTime = new java.util.Date(Long.parseLong(timeStamp) * 1000); // дата java
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String formattedDate = dateFormat.format(dateTime);

            JSONObject main = day.getJSONObject("main");       // блок с параметрами погоды
            String temperature = main.get("temp").toString();  // Температура
            String humidity = main.get("humidity").toString(); // Влажность
            String pressure = main.get("pressure").toString(); // Давление

            JSONArray items = day.getJSONArray("weather"); // выбор массива json по ключу "weather"
            JSONObject weather = items.getJSONObject(0);
            String description =  weather.get("description").toString();   // описание

            weatherList.add(new WeatherData(formattedDate, description,
                    Double.parseDouble(temperature), humidity, pressure));
        }
    }

}
