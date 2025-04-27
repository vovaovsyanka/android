package ru.mirea.ovsyannikov.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

        // Получаем данные из MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView devBookView = findViewById(R.id.textViewDevBook);
            TextView devQuoteView = findViewById(R.id.textViewDevQuote);

            String book_name = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quotes_name = extras.getString(MainActivity.QUOTES_KEY);

            devBookView.setText("Любимая книга разработчика: " + book_name);
            devQuoteView.setText("Цитата из книги: " + quotes_name);
        }
    }

    public void sendDataBack(View view) {
        EditText userBookEditText = findViewById(R.id.editTextUserBook);
        EditText userQuoteEditText = findViewById(R.id.editTextUserQuote);

        String book = userBookEditText.getText().toString();
        String quote = userQuoteEditText.getText().toString();

        // Формируем результат
        String result = String.format(
                "Название Вашей любимой книги: %s\nЦитата: %s",
                book,
                quote
        );

        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, result);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}