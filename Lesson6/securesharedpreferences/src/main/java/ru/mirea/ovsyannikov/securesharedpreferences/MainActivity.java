package ru.mirea.ovsyannikov.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    private TextView poetNameTextView;
    private SharedPreferences secureSharedPreferences;
    private ImageView poetImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poetNameTextView = findViewById(R.id.poetNameTextView);
        poetImageView = findViewById(R.id.poetImageView);
        Button saveButton = findViewById(R.id.saveButton);

        try {
            String mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        View();

        saveButton.setOnClickListener(v -> {
            try {
                secureSharedPreferences.edit()
                        .putString("POET_NAME", "Александр Пушкин")
                        .putString("POET_IMAGE", "poet_image.png")
                        .apply();
                View();
                Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void View(){
        String poetName = secureSharedPreferences.getString("POET_NAME", "Неизвестный поэт");
        String poetImage = secureSharedPreferences.getString("POET_IMAGE", "");

        poetNameTextView.setText("Любимый поэт: " + poetName);

        if (!poetImage.isEmpty()) {
            String resourceName = poetImage.replace(".png", "");
            int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
            poetImageView.setImageResource(resId);
        }
    }
}