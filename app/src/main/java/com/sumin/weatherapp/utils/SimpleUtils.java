package com.sumin.weatherapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SimpleUtils {
    public static String getDateAndTime(long seconds) {
        return new SimpleDateFormat("d MMMM H:00", Locale.getDefault()).format(new Date(seconds * 1000));
    }

    public static String translate(String word) {
        switch (word) {
            case "clear":
                return "Ясно";
            case "partly-cloudy":
                return "Малооблачно";
            case "cloudy":
                return "Облачно с прояснениями";
            case "overcast":
                return "Пасмурно";
            case "partly-cloudy-and-light-rain":
                return "Небольшой дождь";
            case "overcast-and-rain":
                return "Сильный дождь";
            case "overcast-thunderstorms-with-rain":
                return "Сильный дождь, гроза";
            case "cloudy-and-light-rain":
                return "Небольшой дождь";
            case "overcast-and-light-rain":
                return "Небольшой дождь";
            case "cloudy-and-rain":
                return "Дождь";
            case "overcast-and-wet-snow":
                return "Дождь со снегом";
            case "partly-cloudy-and-light-snow":
                return "Небольшой снег";
            case "partly-cloudy-and-snow":
                return "Снег";
            case "overcast-and-snow":
                return "Снегопад";
            case "cloudy-and-light-snow":
                return "Небольшой снег";
            case "overcast-and-light-snow":
                return "Небольшой снег";
            case "cloudy-and-snow":
                return "Снег";
            case "nw":
                return "сз";
            case "n":
                return "с";
            case "ne":
                return "св";
            case "e":
                return "в";
            case "se":
                return "юв";
            case "s":
                return "ю";
            case "sw":
                return "юз";
            case "w":
                return "з";
            case "c":
                return "ш";
        }
        return word;
    }
}


