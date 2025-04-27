package ru.mirea.ovsyannikov.sharer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 1;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        handleFileSelection(data);
                    }
                });

        Button shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> shareText());

        Button pickFileButton = findViewById(R.id.pickFileButton);
        pickFileButton.setOnClickListener(v -> pickFileWithNewApi());

        Button pickFileOldButton = findViewById(R.id.pickFileOldButton);
        pickFileOldButton.setOnClickListener(v -> pickFileWithOldMethod());
    }

    private void shareText() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Привет от MIREA!");
        startActivity(Intent.createChooser(shareIntent, "Поделиться через"));
    }

    private void pickFileWithNewApi() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");
        filePickerLauncher.launch(intent);
    }

    private void pickFileWithOldMethod() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            handleFileSelection(data);
        }
    }

    private void handleFileSelection(Intent data) {
        if (data != null && data.getData() != null) {
            Uri selectedFileUri = data.getData();
            String fileInfo = "Выбранный файл: " + selectedFileUri.toString();
            resultTextView.setText(fileInfo);
            Log.d("MainActivity", fileInfo);
        }
    }
}