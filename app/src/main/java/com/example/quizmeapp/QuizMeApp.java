package com.example.quizmeapp;

import android.app.Application;

import com.example.quizmeapp.DB.AppDB;
import com.reactiveandroid.ReActiveAndroid;
import com.reactiveandroid.ReActiveConfig;
import com.reactiveandroid.internal.database.DatabaseConfig;

public class QuizMeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseConfig appDatabaseConfig = new DatabaseConfig.Builder(AppDB.class)
                .build();

        ReActiveAndroid.init(new ReActiveConfig.Builder(this)
                .addDatabaseConfigs(appDatabaseConfig)
                .build());
    }
}
