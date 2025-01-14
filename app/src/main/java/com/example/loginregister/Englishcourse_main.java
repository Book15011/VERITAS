package com.example.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Englishcourse_main extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private boolean isDoubleClickA = false;
    private boolean isDoubleClickB = false;
    private boolean isDoubleClickE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_englishcourse_main);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(Englishcourse_main.this, "Language not supported.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Englishcourse_main.this, "Text-to-Speech initialization failed.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnA = findViewById(R.id.btnBgSelector1);
        Button btnB = findViewById(R.id.btnBgSelector2);


        btnA.setOnClickListener(v -> {
            if (isDoubleClickA) {
                Intent intent = new Intent(Englishcourse_main.this, Englishcourse_Alphabets.class);
                startActivity(intent);
                isDoubleClickA = false;
            } else {
                if (textToSpeech != null) {
                    textToSpeech.speak(btnA.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                }
                isDoubleClickA = true;
                new Handler().postDelayed(() -> isDoubleClickA = false, 500);
            }
        });

        btnB.setOnClickListener(v -> {
            if (isDoubleClickB) {
                Intent intent = new Intent(Englishcourse_main.this, Englishcourse_words.class);
                startActivity(intent);
                isDoubleClickB = false;
            } else {
                if (textToSpeech != null) {
                    textToSpeech.speak(btnB.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                }
                isDoubleClickB = true;
                new Handler().postDelayed(() -> isDoubleClickB = false, 500);
            }
        });



        TextView textView = findViewById(R.id.text3);

        Intent intent = getIntent();
        boolean isTtsEnabled = intent.getBooleanExtra("tts_enabled", false);

        if (isTtsEnabled) {
            textView.setOnClickListener(v -> speakText(textView.getText().toString()));
        } else {
            Toast.makeText(this, "Text-to-Speech is not enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    private void speakText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Toast.makeText(this, "Text-to-Speech is not available on your device.", Toast.LENGTH_SHORT).show();
        }
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
