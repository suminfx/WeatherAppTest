package com.sumin.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumin.weatherapp.data.City;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private ArrayList<City> cities = new ArrayList<>();
    private OnCityClickListener onCityClickListener;

    public CityAdapter(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

    public CityAdapter() {
    }

    interface OnCityClickListener {
        void onCityClick(City city);
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_item, viewGroup, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder cityViewHolder, int i) {
        cityViewHolder.textViewSelectableCity.setText(cities.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class CityViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewSelectableCity;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSelectableCity = itemView.findViewById(R.id.textViewSelectableCity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCityClickListener != null) {
                        City city = cities.get(getAdapterPosition());
                        onCityClickListener.onCityClick(city);
                    }
                }
            });
        }
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }

    public void addCity(City city) {
        this.cities.add(city);
        notifyDataSetChanged();
    }

    public void clear() {
        cities.clear();
        notifyDataSetChanged();
    }

    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }
}
