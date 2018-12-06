package com.sumin.weatherapp.data;

public class WeatherEntry {

    private long date;
    private double temp;
    private String icon;
    private String condition;
    private double windSpeed;
    private String windDirectory;
    private double pressure;
    private double humidity;
    private String city;

    public WeatherEntry(long date, double temp, String icon, String condition, double windSpeed, String windDirectory, double pressure, double humidity, String city) {
        this.date = date;
        this.temp = temp;
        this.icon = icon;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.windDirectory = windDirectory;
        this.pressure = pressure;
        this.humidity = humidity;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirectory() {
        return windDirectory;
    }

    public void setWindDirectory(String windDirectory) {
        this.windDirectory = windDirectory;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
