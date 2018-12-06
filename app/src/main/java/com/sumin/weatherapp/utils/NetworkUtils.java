package com.sumin.weatherapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {


    private static final int REQUEST_TIMEOUT = 3000;
    public static final String KEY_LOADER_URL = "url";

    private static final String API_KEY = "ad248f15-2840-4dd6-a98f-f609060519fa";
    private static final String BASE_URL_FORECAST = "https://api.weather.yandex.ru/v1/forecast";
    public static final String BASE_IMAGE_URL = "https://yastatic.net/weather/i/icons/blueye/color/svg/";
    public static final String BASE_IMAGE_URL_END = ".svg";
    private static final String API_KEY_PARAMS = "X-Yandex-API-Key";
    private static final String LATITUDE_PARAMS = "lat";
    private static final String LONGITUDE_PARAMS = "lon";

    public static final String LAT_TEST = "55.75396";
    public static final String LON_TEST = "37.620393";

    public static JSONObject getJSONFromNetwork() {
        return getJSONFromNetwork(LAT_TEST, LON_TEST);
    }

    public static JSONObject getJSONFromNetwork(double latitude, double longitude) {
        return getJSONFromNetwork(Double.toString(latitude), Double.toString(longitude));
    }

    public static JSONObject getJSONFromNetwork(String latitude, String longitude) {
        JSONObject result = null;
        try {
            result = new GetDataTask().execute(buildURL(latitude, longitude)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static URL buildURL(double latitude, double longitude) {
        return buildURL(String.valueOf(latitude), String.valueOf(longitude));
    }

    public static URL buildURL(String latitude, String longitude) {
        Uri uri = Uri.parse(BASE_URL_FORECAST).buildUpon()
                .appendQueryParameter(LATITUDE_PARAMS, latitude)
                .appendQueryParameter(LONGITUDE_PARAMS, longitude)
                .build();
        URL result = null;
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetDataTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            if (urls == null || urls.length == 0) {
                return null;
            }
            JSONObject result = null;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                connection.setRequestProperty(API_KEY_PARAMS, API_KEY);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builderResult = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    builderResult.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builderResult.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }

    public interface LoadingProcessListener {
        void onLoadingStart();
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {

        private LoadingProcessListener loadingProcessListener;
        private Bundle bundle;

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle, LoadingProcessListener loadingProcessListener) {
            super(context);
            this.loadingProcessListener = loadingProcessListener;
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (loadingProcessListener != null) {
                loadingProcessListener.onLoadingStart();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle == null || !bundle.containsKey(KEY_LOADER_URL)) {
                return null;
            }
            String urlAsString = bundle.getString(KEY_LOADER_URL);
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null) {
                return null;
            }
            JSONObject result = null;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(REQUEST_TIMEOUT);
                connection.setConnectTimeout(REQUEST_TIMEOUT);
                connection.setRequestProperty(API_KEY_PARAMS, API_KEY);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builderResult = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    builderResult.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builderResult.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }

    public static boolean isInternetConnection(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
