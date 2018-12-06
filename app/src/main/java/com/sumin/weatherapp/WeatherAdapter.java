package com.sumin.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.sumin.weatherapp.data.WeatherEntry;
import com.sumin.weatherapp.utils.SimpleUtils;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Activity activity;
    private ArrayList<WeatherEntry> weatherEntries = new ArrayList<>();

    public WeatherAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_item, viewGroup, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder weatherViewHolder, int i) {
        WeatherEntry weatherEntry = weatherEntries.get(i);
        String temp = (int) weatherEntry.getTemp() + "°";
        weatherViewHolder.textViewTemperature.setText(temp);
        String date = SimpleUtils.getDateAndTime(weatherEntry.getDate());
        weatherViewHolder.textViewDate.setText(date);
        GlideToVectorYou.justLoadImage(activity, Uri.parse(weatherEntry.getIcon()), weatherViewHolder.imageViewIcon);
    }

    @Override
    public int getItemCount() {
        return weatherEntries.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewIcon;
        private TextView textViewDate;
        private TextView textViewTemperature;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTemperature = itemView.findViewById(R.id.textViewTemperature);
        }
    }

    public void setWeatherEntries(ArrayList<WeatherEntry> weatherEntries) {
        this.weatherEntries = weatherEntries;
        notifyDataSetChanged();
    }

    public void addWeatherEntries(WeatherEntry weatherEntry) {
        this.weatherEntries.add(weatherEntry);
        notifyDataSetChanged();
    }

    public void clear() {
        this.weatherEntries.clear();
        notifyDataSetChanged();
    }
}
