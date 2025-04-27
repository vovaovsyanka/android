package ru.mirea.ovsyannikov.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText editTextAge;
    private EditText editTextJob;
    private Button submitButton;

    private MyLooper myLooper;
    private Handler uiHandler = new Handler(Looper.getMainLooper(), msg -> {
        String result = msg.getData().getString("result");
        Log.d(TAG, result);
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextAge    = findViewById(R.id.editTextAge);
        editTextJob    = findViewById(R.id.editTextJob);
        submitButton   = findViewById(R.id.submitButton);

        myLooper = new MyLooper(uiHandler);
        myLooper.start();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ageStr = editTextAge.getText().toString().trim();
                String job    = editTextJob.getText().toString().trim();
                if (ageStr.isEmpty() || job.isEmpty()) {
                    Log.d(TAG, "Оба поля должны быть заполнены");
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageStr);
                } catch (NumberFormatException e) {
                    Log.d(TAG, "Неверный формат возраста");
                    return;
                }
                myLooper.waitUntilReady();

                Message msg = Message.obtain();
                Bundle data = new Bundle();
                data.putInt("age", age);
                data.putString("job", job);
                msg.setData(data);
                myLooper.getWorkerHandler().sendMessage(msg);
            }
        });
    }
}
