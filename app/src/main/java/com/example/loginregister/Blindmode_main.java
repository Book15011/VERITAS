package com.example.loginregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Blindmode_main extends Activity {

    private TextToSpeech textToSpeech;
    private long lastClickTime = 0;
    private Map<Integer, String> layoutTextMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindmode_main);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(Blindmode_main.this, "Text to speech not supported.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Blindmode_main.this, "Initialization failed.", Toast.LENGTH_SHORT).show();
            }
        });

        layoutTextMap = new HashMap<>();
        layoutTextMap.put(R.id.about, "About This Feature");
        layoutTextMap.put(R.id.teachBlind, "How To Teach Blind");
        layoutTextMap.put(R.id.englishMaterials, "English Materials");
        layoutTextMap.put(R.id.braillePractice, "Braille Practice");

        LinearLayout aboutLayout = findViewById(R.id.about);
        LinearLayout teachBlindLayout = findViewById(R.id.teachBlind);
        LinearLayout englishMaterialsLayout = findViewById(R.id.englishMaterials);
        LinearLayout braillePracticeLayout = findViewById(R.id.braillePractice);

        aboutLayout.setOnClickListener(v -> speakText(layoutTextMap.get(R.id.about)));
        setDoubleClickBehavior(teachBlindLayout, layoutTextMap.get(R.id.teachBlind), Blindmode_tblind.class);
        setDoubleClickBehavior(englishMaterialsLayout, layoutTextMap.get(R.id.englishMaterials), Englishcourse_main.class);
        setDoubleClickBehavior(braillePracticeLayout, layoutTextMap.get(R.id.braillePractice), Braille_main.class);
    }

    private void setDoubleClickBehavior(LinearLayout layout, String text, Class<?> destinationActivityClass) {
        layout.setOnClickListener(v -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < 1000) {
                // Double click action
                Intent intent = new Intent(Blindmode_main.this, destinationActivityClass);
                startActivity(intent);
            } else {
                // Single click action
                speakText(text);
            }
            lastClickTime = currentTime;
        });
    }

    private void speakText(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
