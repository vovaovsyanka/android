package ru.mirea.ovsyannikov.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class MyLooper extends Thread {
    private static final String TAG = "MyLooper";

    private Handler workerHandler;
    private final Handler uiHandler;
    private final CountDownLatch readyLatch = new CountDownLatch(1);

    public MyLooper(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    @Override
    public void run() {
        Looper.prepare();

        workerHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int age = msg.getData().getInt("age");
                String job = msg.getData().getString("job");
                Log.d(TAG, "Запущена обработка: возраст=" + age + ", работа=" + job);

                try {
                    Thread.sleep(age * 1000L);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Сон прерван", e);
                }

                String output = String.format(
                        "Обработка завершена за %d секунд: вы %s, возраст %d",
                        age, job, age
                );

                Message reply = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("result", output);
                reply.setData(bundle);
                uiHandler.sendMessage(reply);
            }
        };

        readyLatch.countDown();

        Looper.loop();
    }

    public void waitUntilReady() {
        try {
            readyLatch.await();
        } catch (InterruptedException ignored) { }
    }

    public Handler getWorkerHandler() {
        return workerHandler;
    }
}
