package ru.mirea.ovsyannikov.sharer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        TextView receivedDataTextView = findViewById(R.id.receivedDataTextView);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent, receivedDataTextView);
            } else if (type.startsWith("image/")) {
                handleSendImage(intent, receivedDataTextView);
            }
        }
    }

    private void handleSendText(Intent intent, TextView textView) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            textView.setText("Получен текст: " + sharedText);
        }
    }

    private void handleSendImage(Intent intent, TextView textView) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            textView.setText("Получено изображение: " + imageUri.toString());
        }
    }
}