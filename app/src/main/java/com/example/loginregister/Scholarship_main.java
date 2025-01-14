package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Scholarship_main extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private long lastClickTime = 0;
    private Map<Integer, String> imageViewTextMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_main);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(Scholarship_main.this, "Text to speech not supported.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Scholarship_main.this, "Initialization failed.", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewTextMap = new HashMap<>();
        imageViewTextMap.put(R.id.button1, "Nation Scholarship  nationality does not matter");
        imageViewTextMap.put(R.id.button2, "International scholarship Explore the Scholarship in your ncountry");
        imageViewTextMap.put(R.id.button3, "Scholarship program Gain a variety of experience");

        ImageView button1 = findViewById(R.id.button1);
        ImageView button2 = findViewById(R.id.button2);


        setDoubleClickBehavior(button1, imageViewTextMap.get(R.id.button1), Scholar_inter_nation_link.class);
        setDoubleClickBehavior(button2, imageViewTextMap.get(R.id.button2), Scholar_national.class);

    }

    private void setDoubleClickBehavior(ImageView imageView, String text, Class<?> destinationActivityClass) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private long lastClickTime = 0;
            private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Time between double clicks

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        long clickTime = System.currentTimeMillis();
                        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                            // Double click detected, navigate to DetailActivity
                            Intent intent = new Intent(Scholarship_main.this, destinationActivityClass);
                            startActivity(intent);
                        } else {
                            // Single click action
                            speakText(text);
                        }
                        lastClickTime = clickTime;
                        break;
                }
                return true;
            }
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
