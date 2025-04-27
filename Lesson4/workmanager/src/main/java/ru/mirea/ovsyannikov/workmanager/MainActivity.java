package ru.mirea.ovsyannikov.workmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constraints taskConstraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        OneTimeWorkRequest uploadTask = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setConstraints(taskConstraints)
                .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueue(uploadTask);
    }
}
