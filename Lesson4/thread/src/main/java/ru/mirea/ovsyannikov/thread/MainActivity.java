package ru.mirea.ovsyannikov.thread;

import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.ovsyannikov.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
    private int calculationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        displayMainThreadInfo();
        setupCalculateButton();
    }

    private void displayMainThreadInfo() {
        Thread mainThread = Thread.currentThread();
        String originalName = mainThread.getName();

        mainThread.setName("Группа: БСБО-04-23, №16, Аниме: Соло левелинг");

        String threadInfo = "Информация о главном потоке:\n" +
                "Изначальное имя: " + originalName + "\n" +
                "Новое имя: " + mainThread.getName() + "\n" +
                "Приоритет: " + mainThread.getPriority();

        binding.tvResult.setText(threadInfo);

        Log.d("ThreadInfo", "Группа потока: " + mainThread.getThreadGroup());
        Log.d("ThreadInfo", "ID потока: " + mainThread.getId());
    }

    private void setupCalculateButton() {
        binding.btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int totalPairs = Integer.parseInt(binding.etTotalPairs.getText().toString());
                    int studyDays = Integer.parseInt(binding.etStudyDays.getText().toString());

                    if (studyDays <= 0) {
                        showError("Количество дней должно быть больше 0");
                        return;
                    }

                    calculationCount++;
                    calculateAverageInBackground(totalPairs, studyDays, calculationCount);
                } catch (NumberFormatException e) {
                    showError("Пожалуйста, введите корректные числа");
                }
            }
        });
    }

    private void calculateAverageInBackground(final int totalPairs, final int studyDays, final int taskId) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                final double average = (double) totalPairs / studyDays;
                final String threadInfo = String.format(
                        "Поток %d: %s (приоритет: %d)",
                        taskId,
                        Thread.currentThread().getName(),
                        Process.getThreadPriority(Process.myTid())
                );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String result = String.format(
                                "\n\nСреднее: %.2f пар/день\n%s",
                                average,
                                threadInfo
                        );
                        binding.tvResult.append(result);
                    }
                });

                Log.d("Calculation", "Задача " + taskId + " завершена. " + threadInfo);
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        binding.tvResult.append("\n\nОшибка: " + message);
    }

    @Override
    protected void onDestroy() {
        backgroundExecutor.shutdown();
        super.onDestroy();
    }
}