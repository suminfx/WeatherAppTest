package com.sumin.weatherapp.data;

import java.util.ArrayList;

public class City {

    private String name;
    private double latitude;
    private double longitude;

    private City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static ArrayList<City> getWorldCities() {
        ArrayList<City> cities = new ArrayList<>();
        cities.add(new City("Лондон",51.5085300, -0.1257400));
        cities.add(new City("Токио", 35.6895000, 139.6917100));
        cities.add(new City("Париж", 48.8534100, 2.3488000));
        cities.add(new City("Берлин", 52.52000659999999, 13.404953999999975));
        cities.add(new City("Рим",41.9027835,12.496365500000024));
        cities.add(new City("Минск",53.90453979999999,27.561524400000053));
        cities.add(new City("Стамбул",41.0082376,28.97835889999999));
        cities.add(new City("Вашингтон",38.9071923,-77.03687070000001));
        cities.add(new City("Киев", 50.4501,30.523400000000038));
        cities.add(new City("Пекин", 39.90419989999999, 116.40739630000007));
        return cities;
    }

    public static ArrayList<City> getRussianCities() {
        ArrayList<City> cities = new ArrayList<>();
        cities.add(new City("Москва",55.755826,37.617299900000035));
        cities.add(new City("Санкт-Петербург",59.9342802,30.335098600000038));
        cities.add(new City("Новосибирск",55.00835259999999,82.93573270000002));
        cities.add(new City("Екатеринбург",56.83892609999999,60.60570250000001 ));
        cities.add(new City("Нижний Новгород",56.2965039,43.936059));
        cities.add(new City("Казань", 55.8304307,49.06608060000008));
        cities.add(new City("Челябинск",55.1644419,61.4368432));
        cities.add(new City("Омск",54.9884804,73.32423610000001));
        cities.add(new City("Ростов-на-Дону",47.2357137,39.701505));
        cities.add(new City("Уфа",54.7387621,55.972055400000045));
        return cities;
    }
}
