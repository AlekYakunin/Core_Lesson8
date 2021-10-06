public class WeatherData {
    private String localDate;
    private String weatherText;
    private Double temperature;
    private String humidity;
    private String pressure;

    public WeatherData(String localDate, String weatherText, Double temperature, String humidity, String pressure) {
        this.localDate = localDate;
        this.weatherText = weatherText;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public String getLocalDate() {
        return localDate;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public Double getTemperature() { return temperature; }

    public String getHumidity() { return humidity; }

    public String getPressure() { return pressure; }
}
