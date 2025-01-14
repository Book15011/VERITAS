package com.example.loginregister;

import android.app.Application;
import android.speech.tts.TextToSpeech;

public class Blindmode_Myapplication extends Application {

    private TextToSpeech textToSpeech;

    @Override
    public void onCreate() {
        super.onCreate();
        textToSpeech = new TextToSpeech(this, null);
    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    @Override
    public void onTerminate() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onTerminate();
    }
}
