package ru.mirea.ovsyannikovvi.multiactivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.i("SecondActivity", "onCreate");

        textView = findViewById(R.id.textView);

        String text = getIntent().getStringExtra("key");
        if (text != null) {
            textView.setText(text);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("SecondActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("SecondActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("SecondActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("SecondActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("SecondActivity", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("SecondActivity", "onRestart");
    }
}