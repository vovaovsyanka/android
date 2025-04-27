package ru.mirea.ovsyannikov.cryptoloader;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int LOADER_ID = 1001;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.inputText);
        button = findViewById(R.id.buttonEncrypt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter text to encrypt", Toast.LENGTH_SHORT).show();
                    return;
                }

                SecretKey key = CryptoUtils.generateKey();
                byte[] encrypted = CryptoUtils.encryptMsg(input, key);

                Bundle bundle = new Bundle();
                bundle.putByteArray(CryptoLoader.ARG_CIPHER, encrypted);
                bundle.putByteArray(CryptoLoader.ARG_KEY, key.getEncoded());

                LoaderManager.getInstance(MainActivity.this)
                        .restartLoader(LOADER_ID, bundle, MainActivity.this);
            }
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LOADER_ID && args != null) {
            Toast.makeText(this, "Decrypting...", Toast.LENGTH_SHORT).show();
            return new CryptoLoader(this, args);
        }
        throw new IllegalArgumentException("Invalid loader ID");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        Toast.makeText(this, "Decrypted: " + data, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }
}