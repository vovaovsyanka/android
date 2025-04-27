package ru.mirea.ovsyannikov.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadWorker extends Worker {
    private static final String TAG = "UploadTask";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Task started");

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 10_000) {
            // Просто ждём 10 секунд без использования sleep
        }

        Log.i(TAG, "Task finished");
        return Result.success();
    }
}
