package ru.mirea.ovsyannikov.httpurlconnection;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkService {

    public interface DataCallback {
        void onDataReceived(JSONObject ipData, JSONObject weatherData);
        void onError(String message);
    }

    public void fetchAllData(Context context, DataCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                String ipJson = fetchUrl("https://ipinfo.io/json");
                JSONObject ipData = new JSONObject(ipJson);

                String[] coords = ipData.getString("loc").split(",");
                String weatherUrl = String.format(
                        "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                        coords[0], coords[1]);

                String weatherJson = fetchUrl(weatherUrl);
                JSONObject weatherData = new JSONObject(weatherJson);

                callback.onDataReceived(ipData, weatherData);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    private String fetchUrl(String urlString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
}