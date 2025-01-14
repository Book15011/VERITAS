package com.example.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.loginregister.ui.QuizOptionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Home extends AppCompatActivity {

    TextView textViewUsername;
    private TextToSpeech textToSpeech;
    private long lastClickTime = 0;
    private Map<Integer, String> buttonTextMap;

    // Firebase related variables
    private FirebaseAuth auth;
    private FirebaseUser user;

    private static final long DOUBLE_CLICK_TIME_DELTA = 500; // Time between double clicks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        textViewUsername = findViewById(R.id.user_details);
        // Firebase authentication initialization
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            textViewUsername.setText(user.getEmail());
        }
        Button logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(Home.this, "Text to speech not supported.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Home.this, "Initialization failed.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonTextMap = new HashMap<>();
        buttonTextMap.put(R.id.blind, "Blind Mode");
        buttonTextMap.put(R.id.scholarship, "Scholarship");
        buttonTextMap.put(R.id.clothingName, "Quality education");
        buttonTextMap.put(R.id.academic, "Academic test");
        buttonTextMap.put(R.id.comunication, "artificial intelligence teacher");
        buttonTextMap.put(R.id.track, "Self tracking");
        // Add other buttons and corresponding text here

        CardView blindButton = findViewById(R.id.blind);
        CardView scholarshipButton = findViewById(R.id.scholarship);
        CardView qualityEducationButton = findViewById(R.id.quality_education);
        CardView academicTestButton = findViewById(R.id.academic_level_test);
        CardView aiTeacherButton = findViewById(R.id.concept_summary);
        CardView selfTrackingButton = findViewById(R.id.self_tracking);
        // Find other buttons here

        setDoubleClickBehavior(blindButton, buttonTextMap.get(R.id.blind), Blindmode_main.class);
        setDoubleClickBehavior(scholarshipButton, buttonTextMap.get(R.id.scholarship), Scholarship_main.class);
        setDoubleClickBehavior(qualityEducationButton, buttonTextMap.get(R.id.clothingName), QualityEducation.class);
        setDoubleClickBehavior(academicTestButton, buttonTextMap.get(R.id.academic), QuizOptionActivity.class);
        setDoubleClickBehavior(aiTeacherButton, buttonTextMap.get(R.id.comunication), Ai_teacher.class);
        setDoubleClickBehavior(selfTrackingButton, buttonTextMap.get(R.id.track), SelfTracking_main.class);
        // Set double-click behavior for other buttons here
    }

    private void setDoubleClickBehavior(CardView button, String text, Class<?> destinationActivityClass) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        long clickTime = System.currentTimeMillis();
                        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                            // Double click detected, navigate to destination activity
                            if (user != null) {
                                Intent intent = new Intent(Home.this, destinationActivityClass);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Home.this, "Please log in first.", Toast.LENGTH_SHORT).show();
                            }
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
