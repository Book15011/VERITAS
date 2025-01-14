package com.example.loginregister;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Braille_main extends AppCompatActivity implements OnInitListener {
    private boolean[] buttonStates = {false, false, false, false, false, false}; // Initial states

    private TextView brailleTextView;
    private Handler handler = new Handler();
    private Runnable clearRunnable = new Runnable() {
        @Override
        public void run() {
            clearInput(); // Call the method to clear input and TextView
        }
    };

    private TextToSpeech textToSpeech;
    private char lastCharacter = ' '; // Initialize with a space character

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braille_main);

        // Initialize TTS
        textToSpeech = new TextToSpeech(this, this);

        brailleTextView = findViewById(R.id.brailleTextView);

        Button button1 = findViewById(R.id.position1);
        Button button2 = findViewById(R.id.position2);
        Button button3 = findViewById(R.id.position3);
        Button button4 = findViewById(R.id.position4);
        Button button5 = findViewById(R.id.position5);
        Button button6 = findViewById(R.id.position6);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(0); // Button 1 is at index 0
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(1); // Button 2 is at index 1
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(2); // Button 3 is at index 2
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(3); // Button 4 is at index 3
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(4); // Button 5 is at index 4
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(5); // Button 6 is at index 5
            }
        });
    }

    private void handleButtonClick(int buttonIndex) {
        buttonStates[buttonIndex] = !buttonStates[buttonIndex]; // Toggle the button state

        // Update the Braille representation based on the button states
        StringBuilder brailleRepresentation = new StringBuilder();
        for (boolean state : buttonStates) {
            brailleRepresentation.append(state ? '1' : '0');
        }

        // Log the raw input data in the terminal
        Log.d("RawInputData", "Raw Input: " + brailleRepresentation);

        // Get the corresponding character for the current Braille dots representation
        char character = getCharacterForBraille(brailleRepresentation.toString());

        // Update the brailleTextView with the corresponding character
        brailleTextView.setText("Character: " + character);

        // Speak out the final character using TTS
        if (textToSpeech != null) {
            textToSpeech.speak(String.valueOf(character), TextToSpeech.QUEUE_ADD, null, null);
        }

        // Reset the countdown timer (clear the previous one and start a new one)
        handler.removeCallbacks(clearRunnable);
        handler.postDelayed(clearRunnable, 3000); // 3000 milliseconds = 3 seconds
    }

    private void clearInput() {
        // Clear the input and the corresponding TextView
        for (int i = 0; i < buttonStates.length; i++) {
            buttonStates[i] = false;
        }

        // Clear the TextView
        brailleTextView.setText("Character: ");
    }

    private char getCharacterForBraille(String brailleRepresentation) {
        if (brailleRepresentation == null) {
            return '?'; // Return '?' if the Braille representation is null
        }

        for (char character : Braille_ref.getBrailleMap().keySet()) {
            if (brailleRepresentation.equals(Braille_ref.getBrailleRepresentation(character))) {
                return character;
            }
        }
        return '?'; // Return '?' if the Braille representation is not found in the map
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // TTS initialization successful
            // You can add any additional setup here if required.
            int result = textToSpeech.setLanguage(Locale.US); // Set TTS language to US English
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // TTS language data is missing or not supported
                // Handle this situation if needed.
            }
        } else {
            // TTS initialization failed, handle the error if needed.
            // You can display a message to the user indicating TTS initialization failed.
        }
    }

    @Override
    protected void onDestroy() {
        // Release the Text-to-Speech resources when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}