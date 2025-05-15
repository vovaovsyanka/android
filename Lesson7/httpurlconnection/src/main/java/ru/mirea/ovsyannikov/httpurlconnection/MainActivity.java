package ru.mirea.ovsyannikov.httpurlconnection;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NetworkService.DataCallback {

    private TextView tvIp, tvLoc, tvWeather;
    private NetworkService networkService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupNetworkService();
        setupProgressDialog();
        setupButtonListener();
    }

    private void initViews() {
        tvIp = findViewById(R.id.tv_ip);
        tvLoc = findViewById(R.id.tv_loc);
        tvWeather = findViewById(R.id.tv_weather);
    }

    private void setupNetworkService() {
        networkService = new NetworkService();
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Получаем данные...");
        progressDialog.setCancelable(false);
    }

    private void setupButtonListener() {
        Button btn = findViewById(R.id.btn_load);
        btn.setOnClickListener(v -> checkConnectionAndLoad());
    }

    private void checkConnectionAndLoad() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;

        if (netInfo != null && netInfo.isConnected()) {
            progressDialog.show();
            networkService.fetchAllData(this, this);
        } else {
            showToast("Нет интернет-соединения");
        }
    }

    @Override
    public void onDataReceived(JSONObject ipData, JSONObject weatherData) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            updateUI(ipData, weatherData);
        });
    }

    @Override
    public void onError(String message) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            showToast("Ошибка: " + message);
        });
    }

    private void updateUI(JSONObject ipData, JSONObject weatherData) {
        try {
            tvIp.setText(ipData.getString("ip"));
            tvLoc.setText(String.format("%s, %s",
                    ipData.optString("city"),
                    ipData.optString("country")));

            if (weatherData != null) {
                JSONObject currentWeather = weatherData.getJSONObject("current_weather");
                String temp = currentWeather.getDouble("temperature") + "°C";
                tvWeather.setText(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}