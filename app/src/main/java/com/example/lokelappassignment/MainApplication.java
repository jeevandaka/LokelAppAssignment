package com.example.lokelappassignment;

import android.app.Application;
import android.util.Log;

import com.example.lokelappassignment.data.AppDataManager;
import com.example.lokelappassignment.data.Local.db.AppDatabase;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();
    private static MainApplication instance;
    private static AppDatabase appDatabase;
    private static AppDataManager appDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = AppDatabase.getInstance(instance);
        appDataManager = AppDataManager.getInstance(instance);
        Log.d(TAG, "onCreate: ");
    }

    public static MainApplication getInstance() {
        return instance;
    }
    public static AppDataManager getAppDataManager() {
        return appDataManager;
    }
    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
