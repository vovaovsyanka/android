package ru.mirea.ovsyannikov.timeservice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView tvResult;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);
        Button btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            TimeFetcher.getTime(new TimeFetcher.TimeCallback() {
                @Override
                public void onSuccess(String time) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        tvResult.setText(time);
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        tvResult.setText(message);
                    });
                }
            });
        });
    }
}