package ru.mirea.ovsyannikov.timeservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TimeFetcher {
    public interface TimeCallback {
        void onSuccess(String time);
        void onError(String message);
    }

    public static void getTime(TimeCallback callback) {
        new Thread(() -> {
            try (Socket socket = new Socket("time.nist.gov", 13);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }

                if (response.length() == 0) {
                    callback.onError("Пустой ответ");
                    return;
                }

                String[] data = response.toString().trim().split("\\s+");
                if (data.length >= 3) {
                    String timeData = "Дата: " + data[1] + "\nВремя: " + data[2];
                    callback.onSuccess(timeData);
                } else {
                    callback.onError("Ошибка парсинга");
                }

            } catch (IOException e) {
                callback.onError("Ошибка: " + e.getMessage());
            }
        }).start();
    }
}