package com.sumin.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.sumin.weatherapp.data.City;
import com.sumin.weatherapp.data.WeatherEntry;
import com.sumin.weatherapp.utils.JSONUtils;
import com.sumin.weatherapp.utils.NetworkUtils;
import com.sumin.weatherapp.utils.SimpleUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    //Getting user's location
    private static final int REQUEST_PERMISSION_LOCATION_CODE = 122;
    private LocationManager locationManager;
    private LocationListener locationListener;

    //Data
    private double userLatitude;
    private double userLongitude;
    private String nameOfCity;

    //Views
    private RecyclerView recyclerViewWeather;
    private WeatherAdapter weatherAdapter;
    private RecyclerView recyclerViewCities;
    private CityAdapter cityAdapter;
    private ImageView imageViewWeatherIcon;
    private TextView textViewTemp;
    private TextView textViewCity;
    private TextView textViewHumidity;
    private TextView textViewPressure;
    private TextView textViewWind;
    private TextView textViewWeatherDescription;
    private TextView textViewForToday;
    private TextView textViewForTomorrow;
    private ImageView imageViewIconBack;
    private ImageView imageViewIconTomorrow;
    private SwitchCompat switchCities;
    private ScrollView scrollView;
    private ProgressBar progressBarLoading;

    //Weather
    private boolean todayWeather = true;
    private boolean getForecast = false;
    private String tag;

    //Download data
    private LoaderManager loaderManager;
    private static final int LOADER_ID = 155;

    //Hide details
    private boolean selectedToday = false;
    private boolean selectedTomorrow = false;
    private boolean selectedThreeDays = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //Init views
        imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon);
        textViewTemp = findViewById(R.id.textViewTemp);
        textViewCity = findViewById(R.id.textViewCity);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewPressure = findViewById(R.id.textViewPressure);
        textViewWind = findViewById(R.id.textViewWind);
        textViewWeatherDescription = findViewById(R.id.textViewDescription);
        textViewForToday = findViewById(R.id.textViewForToday);
        textViewForTomorrow = findViewById(R.id.textViewForTomorrow);
        imageViewIconBack = findViewById(R.id.imageViewIconBack);
        imageViewIconTomorrow = findViewById(R.id.imageViewIconTomorrow);
        recyclerViewWeather = findViewById(R.id.recyclerViewWeather);
        switchCities = findViewById(R.id.switchCities);
        scrollView = findViewById(R.id.mainScrollView);
        progressBarLoading = findViewById(R.id.progressBarLoading);

        setDummyData();

        //Adapters
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(this);
        recyclerViewWeather.setAdapter(weatherAdapter);
        recyclerViewCities = findViewById(R.id.recyclerViewCities);
        cityAdapter = new CityAdapter(new CityAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(City city) {
                dispatchListener();
                clearDetailForecast();
                scrollView.scrollTo(0,0);
                userLatitude = city.getLatitude();
                userLongitude = city.getLongitude();
                nameOfCity = city.getName();
                loadWeather();
            }
        });
        recyclerViewCities.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewCities.setAdapter(cityAdapter);
        switchCities.setChecked(true);
        switchCities.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cityAdapter.setCities(City.getWorldCities());
                } else {
                    cityAdapter.setCities(City.getRussianCities());
                }
            }
        });
        switchCities.setChecked(false);

        //Loaders
        loaderManager = LoaderManager.getInstance(this);
        setLocationByUserPosition();
        loadWeather();
    }

    private void setDummyData() {
        userLatitude = Double.parseDouble(NetworkUtils.LAT_TEST);
        userLongitude = Double.parseDouble(NetworkUtils.LON_TEST);
        nameOfCity = getString(R.string.city_test);
        tag = getString(R.string.tag_today);
    }

    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1000, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
                nameOfCity = getString(R.string.my_city);
            }
            loadWeather();
            return true;
        } else {
            return false;
        }
    }

    private void clearDetailForecast() {
        weatherAdapter.clear();
        recyclerViewWeather.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == REQUEST_PERMISSION_LOCATION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    private void loadWeather() {
        if (!NetworkUtils.isInternetConnection(this)) {
            Toast.makeText(this, R.string.warning_no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        URL url = NetworkUtils.buildURL(userLatitude, userLongitude);
        Bundle bundle = new Bundle();
        bundle.putString(NetworkUtils.KEY_LOADER_URL, url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    public void onClickShowTomorrowWeather(View view) {
        showTomorrowWeather();
    }

    public void onClickShowTodayWeather(View view) {
        showTodayWeather();
    }

    private void showTodayWeather() {
        textViewForToday.setVisibility(View.INVISIBLE);
        imageViewIconBack.setVisibility(View.INVISIBLE);
        textViewForTomorrow.setVisibility(View.VISIBLE);
        imageViewIconTomorrow.setVisibility(View.VISIBLE);
        todayWeather = true;
        loadWeather();
    }

    private void showTomorrowWeather() {
        textViewForTomorrow.setVisibility(View.INVISIBLE);
        imageViewIconTomorrow.setVisibility(View.INVISIBLE);
        textViewForToday.setVisibility(View.VISIBLE);
        imageViewIconBack.setVisibility(View.VISIBLE);
        todayWeather = false;
        loadWeather();
    }

    public void onClickShowWeatherDetail(View view) {
        tag = (String) view.getTag();
        String tagToday = getResources().getString(R.string.tag_today);
        String tagTomorrow = getResources().getString(R.string.tag_tomorrow);
        if (tag.equals(tagToday)) {
            if (selectedToday) {
                clearDetailForecast();
                selectedToday = false;
                return;
            } else {
                selectedThreeDays = false;
                selectedTomorrow = false;
                selectedToday = true;
            }
        } else if (tag.equals(tagTomorrow)) {
            if (selectedTomorrow) {
                clearDetailForecast();
                selectedTomorrow = false;
                return;
            } else {
                selectedThreeDays = false;
                selectedTomorrow = true;
                selectedToday = false;
            }
        } else {
            if (selectedThreeDays) {
                clearDetailForecast();
                selectedThreeDays = false;
                return;
            } else {
                selectedThreeDays = true;
                selectedTomorrow = false;
                selectedToday = false;
            }
        }
        getForecast = true;
        loadWeather();
    }

    private void setLocationByUserPosition() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
                nameOfCity = getString(R.string.my_city);
                loadWeather();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (!requestPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION_CODE);
        }
    }

    public void onClickShowMyWeather(View view) {
        setLocationByUserPosition();
    }

    private void dispatchListener()  {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new NetworkUtils.JSONLoader(this, bundle, new NetworkUtils.LoadingProcessListener() {
            @Override
            public void onLoadingStart() {
                progressBarLoading.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject jsonObject) {
        if (jsonObject == null) {
            Toast.makeText(this, R.string.warning_slow_internet_connection, Toast.LENGTH_SHORT).show();
        }
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
        if (getForecast) {
            setForecastWeather(jsonObject, tag);
            getForecast = false;
        } else {
            setCurrentWeather(jsonObject);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }

    private void setCurrentWeather(JSONObject jsonObject) {
        WeatherEntry weatherEntry;
        if (todayWeather) {
            weatherEntry = JSONUtils.getWeatherEntryFromJSON(jsonObject);
        } else {
            weatherEntry = JSONUtils.getWeatherEntryForTomorrow(jsonObject);
        }
        if (weatherEntry != null) {
            textViewCity.setText(nameOfCity);
            String humidity = (int) weatherEntry.getHumidity() + "%";
            textViewHumidity.setText(humidity);
            String pressure = (int) weatherEntry.getPressure() + "мм";
            textViewPressure.setText(pressure);
            String wind = String.format("%s м/с, %s", (float) weatherEntry.getWindSpeed(), SimpleUtils.translate(weatherEntry.getWindDirectory()));
            textViewWind.setText(wind);
            String temperature = (int) weatherEntry.getTemp() + "°";
            textViewTemp.setText(temperature);
            textViewWeatherDescription.setText(SimpleUtils.translate(weatherEntry.getCondition()));
            String imageUri = weatherEntry.getIcon();
            GlideToVectorYou.justLoadImage(this, Uri.parse(imageUri), imageViewWeatherIcon);
        }
    }

    private void setForecastWeather(JSONObject jsonObject, String tag) {
        String tagToday = getResources().getString(R.string.tag_today);
        String tagTomorrow = getResources().getString(R.string.tag_tomorrow);
        ArrayList<WeatherEntry> weatherEntries;
        if (tag.equals(tagToday)) {
            showTodayWeather();
            weatherEntries = JSONUtils.getForecastToday(jsonObject);
        } else if (tag.equals(tagTomorrow)) {
            showTomorrowWeather();
            weatherEntries = JSONUtils.getForecastTomorrow(jsonObject);
        } else {
            showTodayWeather();
            weatherEntries = JSONUtils.getForecastForThreeDays(jsonObject);
        }
        weatherAdapter.setWeatherEntries(weatherEntries);
        recyclerViewWeather.setVisibility(View.VISIBLE);
    }
}
