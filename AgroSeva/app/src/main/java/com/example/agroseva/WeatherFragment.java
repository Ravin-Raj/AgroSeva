package com.example.agroseva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFragment extends Fragment {

    private static final String WEATHER_API_KEY = "bb513cafd6a336940e677f7f200a4009"; // Replace with your OpenWeatherMap API Key

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Spinner citySpinner = view.findViewById(R.id.citySpinner);
        Button getWeatherButton = view.findViewById(R.id.getWeatherButton);
        TextView weatherInfo = view.findViewById(R.id.weatherInfo);

        getWeatherButton.setOnClickListener(v -> {
            String selectedCity = citySpinner.getSelectedItem().toString();
            fetchWeatherData(selectedCity, weatherInfo);
        });
    }

    private void fetchWeatherData(String city, TextView weatherInfo) {
        new Thread(() -> {
            try {
                // Create the API URL
                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + WEATHER_API_KEY + "&units=metric";

                // Open the connection
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse JSON data
                JSONObject jsonResponse = new JSONObject(response.toString());
                String weather = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
                double temp = jsonResponse.getJSONObject("main").getDouble("temp");

                String result = "Weather: " + weather + "\nTemperature: " + temp + "°C";

                // Update the TextView on the main thread
                getActivity().runOnUiThread(() -> weatherInfo.setText(result));

            } catch (Exception e) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
