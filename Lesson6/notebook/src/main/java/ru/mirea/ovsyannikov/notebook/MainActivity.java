package ru.mirea.ovsyannikov.notebook;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText filenameEditText, quoteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filenameEditText = findViewById(R.id.filenameEditText);
        quoteEditText = findViewById(R.id.quoteEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button loadButton = findViewById(R.id.loadButton);

        saveButton.setOnClickListener(v -> {
            if (isExternalStorageWritable()) {
                String filename = filenameEditText.getText().toString() + ".txt";
                String quote = quoteEditText.getText().toString();
                writeFileToExternalStorage(filename, quote);
            } else {
                Toast.makeText(this, "Внешнее хранилище недоступно для записи", Toast.LENGTH_SHORT).show();
            }
        });

        loadButton.setOnClickListener(v -> {
            if (isExternalStorageReadable()) {
                String filename = filenameEditText.getText().toString() + ".txt";
                String quote = readFileFromExternalStorage(filename);
                if (quote != null) {
                    quoteEditText.setText(quote);
                }
            } else {
                Toast.makeText(this, "Внешнее хранилище недоступно для чтения", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private void writeFileToExternalStorage(String filename, String content) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, filename);

        try {
            if (!path.exists()) {
                path.mkdirs();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(content);
            output.close();

            Toast.makeText(this, "Файл сохранен: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show();
            Log.e("Notebook", "Ошибка записи файла", e);
        }
    }

    private String readFileFromExternalStorage(String filename) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, filename);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuilder content = new StringBuilder();
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (!firstLine) {
                    content.append("\n");
                }
                content.append(line);
                firstLine = false;
            }

            reader.close();
            inputStreamReader.close();
            fileInputStream.close();

            return content.toString();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка при чтении файла", Toast.LENGTH_SHORT).show();
            Log.e("Notebook", "Ошибка чтения файла", e);
            return null;
        }
    }
}