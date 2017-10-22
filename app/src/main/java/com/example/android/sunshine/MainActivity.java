package com.example.android.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // Create a field to store the weather display TextView
    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }

    // Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
    private void loadWeatherData() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    // Create a class that extends AsyncTask to perform network requests
    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        // Override the doInBackground method to perform your network requests
        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            String[] simpleJsonWeatherData = null;
            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return simpleJsonWeatherData;
        }

        // Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(String[] weatherData) {

            if (weatherData != null) {
                for (String weatherString : weatherData) {
                    mWeatherTextView.setText(weatherString + "\n\n\n");
                }
            }
        }
    }
}
