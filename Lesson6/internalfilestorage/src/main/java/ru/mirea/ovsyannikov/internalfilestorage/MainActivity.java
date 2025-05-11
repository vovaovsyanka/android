package ru.mirea.ovsyannikov.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private EditText dateEditText, descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateEditText = findViewById(R.id.dateEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String filename = "historical_date.txt";
            String content = dateEditText.getText().toString() + "\n" +
                    descriptionEditText.getText().toString();

            try (FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE)) {
                fos.write(content.getBytes());
                Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        try {
            String filename = "historical_date.txt";
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            String[] parts = sb.toString().split("\n");
            if (parts.length >= 2) {
                dateEditText.setText(parts[0]);
                descriptionEditText.setText(parts[1]);
            }
        } catch (IOException e) {}
    }
}