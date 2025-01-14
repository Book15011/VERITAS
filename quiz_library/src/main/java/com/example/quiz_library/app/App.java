package com.example.quiz_library.app;

import android.app.Application;
import android.content.Context;

import com.example.quiz_library.local_data.QuizPref;
import com.example.quiz_library.local_data.source.Base;

public class App extends Application {
    public static   Context cnt;
    @Override
    public void onCreate() {
        super.onCreate();
        cnt =this;
        Base.init(this);
        QuizPref.init(this);
    }
}
