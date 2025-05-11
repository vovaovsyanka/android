package ru.mirea.ovsyannikov.employeedb;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    private static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "superhero-db")
                .allowMainThreadQueries()
                .build();

        addTestData();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    private void addTestData() {
        EmployeeDao dao = database.employeeDao();

        if (dao.getAll().isEmpty()) {
            dao.insert(new Employee("Человек-паук", 50000, "Паучьи способности"));
            dao.insert(new Employee("Железный человек", 1000000, "Гений, миллиардер, плейбой, филантроп"));
            dao.insert(new Employee("Черная вдова", 75000, "Мастер шпионажа"));
        }
    }
}
