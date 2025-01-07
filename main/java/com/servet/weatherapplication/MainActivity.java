package com.servet.weatherapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import android.widget.Toast; //

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView cityText, temperatureText, weatherText;
    private EditText cityEditText;
    private Button searchCityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = findViewById(R.id.cityText);
        temperatureText = findViewById(R.id.temperatureText);
        weatherText = findViewById(R.id.weatherText);
        cityEditText = findViewById(R.id.cityEditText); // Add this in `activity_main.xml`
        searchCityButton = findViewById(R.id.searchButton); // Add this in `activity_main.xml`

        // Preload example city
        fetchWeatherData("Tarragona");

        // Set up the Search button listener
        searchCityButton.setOnClickListener(v -> {
            String city = cityEditText.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
            } else {
                fetchWeatherData(city); // Fetch weather for the entered city
            }
        });
    }

    private void fetchWeatherData(String city) {
        WeatherService service = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeather(city, "27837c19fdf43b55d6b6639fd110c149", "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    cityText.setText(weatherResponse.getName());
                    temperatureText.setText("Temp: " + weatherResponse.getMain().getTemp() + "°C");
                    weatherText.setText("Condition: " + weatherResponse.getWeather().get(0).getDescription());
                } else {
                    Log.e("API Error", "Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }
}