package ru.mirea.ovsyannikov.mireaproject;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class BackgroundWorker extends Worker {
    private static final String TAG = "BackgroundWorker";

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Log.d(TAG, "Задача запущена");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Log.e(TAG, "Ошибка: ", e);
            return Result.failure();
        }
        Log.d(TAG, "Задача завершена");
        return Result.success();
    }
}

