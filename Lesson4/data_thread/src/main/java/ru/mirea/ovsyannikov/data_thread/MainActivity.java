package ru.mirea.ovsyannikov.data_thread;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.TimeUnit;

import ru.mirea.ovsyannikov.data_thread.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Runnable runn1 = new Runnable() {
            @Override
            public void run() {
                binding.tvInfo.append("\nrunn1: выполнен через runOnUiThread()");
                Log.d("ThreadDemo", "Runn1 executed");
            }
        };

        final Runnable runn2 = new Runnable() {
            @Override
            public void run() {
                binding.tvInfo.append("\nrunn2: выполнен через post()");
                Log.d("ThreadDemo", "Runn2 executed");
            }
        };

        final Runnable runn3 = new Runnable() {
            @Override
            public void run() {
                binding.tvInfo.append("\nrunn3: выполнен через postDelayed(2000)");
                Log.d("ThreadDemo", "Runn3 executed");
            }
        };

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                runOnUiThread(runn1);

                TimeUnit.SECONDS.sleep(1);
                binding.tvInfo.post(runn2);
                binding.tvInfo.postDelayed(runn3, 2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        binding.tvInfo.setText("Особенности методов взаимодействия с UI-потоком:\n\n" +
                "• runOnUiThread():\n" +
                "  - Обеспечивает синхронное выполнение кода в основном потоке\n" +
                "  - Доступен до полной инициализации элементов интерфейса\n\n" +

                "• post():\n" +
                "  - Помещает операцию в очередь обработки сообщений\n" +
                "  - Исполняется асинхронно в ближайшее доступное время\n\n" +

                "• postDelayed():\n" +
                "  - Вариант post() с указанием временной задержки\n" +
                "  - Применяется для планирования отложенных действий\n\n");
    }
}