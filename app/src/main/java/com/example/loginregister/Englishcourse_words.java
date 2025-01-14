package com.example.loginregister;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Englishcourse_words extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private TextView wordTextView;
    private TextView definitionTextView;
    private Button nextButton;
    private Button backButton;
    private Spinner languageSpinner;

    private List<Englishcourse_words_fc> flashcards;
    private int currentFlashcardIndex = 0;
    private boolean isEnglishLanguageSelected = true;
    private boolean isThaiLanguageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_englishcourse_word);

        // Initialize UI elements
        wordTextView = findViewById(R.id.wordTextView);
        definitionTextView = findViewById(R.id.definitionTextView);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        languageSpinner = findViewById(R.id.languageSpinner);
        textToSpeech = new TextToSpeech(this, this);

        // Create the custom ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.languages,
                R.layout.flashcard_spinner // Use the custom layout for the spinner item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(spinnerAdapter);

        // Load flashcard data (replace with your actual data loading method)
        flashcards = loadFlashcardData();

        // Set initial word and definition
        showFlashcard();

        nextButton.setOnClickListener(v -> {
            nextFlashcard();
        });

        backButton.setOnClickListener(v -> {
            previousFlashcard();
        });

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();

                if (selectedLanguage.equals("English")) {
                    isEnglishLanguageSelected = true;
                    isThaiLanguageSelected = false;
                    textToSpeech.setLanguage(Locale.US);
                } else if (selectedLanguage.equals("Thai")) {
                    isEnglishLanguageSelected = false;
                    isThaiLanguageSelected = true;
                    textToSpeech.setLanguage(new Locale("th"));
                } else if (selectedLanguage.equals("Korean")) {
                    isEnglishLanguageSelected = false;
                    isThaiLanguageSelected = false;
                    textToSpeech.setLanguage(Locale.KOREAN);
                }

                showFlashcard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language for TTS
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle language not supported or missing data error if needed
            }
        } else {
            // Handle TTS initialization error if needed
        }
    }

    private void showFlashcard() {
        Englishcourse_words_fc currentFlashcard = flashcards.get(currentFlashcardIndex);
        if (isEnglishLanguageSelected) {
            wordTextView.setText(currentFlashcard.getWord());
            definitionTextView.setText(currentFlashcard.getEnglishDefinition());
            textToSpeech.speak(currentFlashcard.getEnglishDefinition(), TextToSpeech.QUEUE_FLUSH, null, null);
        } else if (isThaiLanguageSelected) {
            wordTextView.setText(currentFlashcard.getWord());
            definitionTextView.setText(currentFlashcard.getThaiDefinition());
            textToSpeech.speak(currentFlashcard.getThaiDefinition(), TextToSpeech.QUEUE_FLUSH, null, null);
        } else { // For "Korean" language
            wordTextView.setText(currentFlashcard.getWord());
            definitionTextView.setText(currentFlashcard.getKoreanDefinition());
            textToSpeech.speak(currentFlashcard.getKoreanDefinition(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void nextFlashcard() {
        if (currentFlashcardIndex < flashcards.size() - 1) {
            currentFlashcardIndex++;
            showFlashcard();
        }
    }

    private void previousFlashcard() {
        if (currentFlashcardIndex > 0) {
            currentFlashcardIndex--;
            showFlashcard();
        }
    }

    private List<Englishcourse_words_fc> loadFlashcardData() {
        List<Englishcourse_words_fc> flashcards = new ArrayList<>();

        flashcards.add(new Englishcourse_words_fc("Apple", "Apple Spell a p p l e. A round fruit with red or green skin that is white inside.", "Apple สะกด a p p l e คือ ผลไม้ทรงกลมที่มีสีเขียวหรือสีแดง และเนื้อเป็นสีขาว","Apple 스펠 a p p l e 사과는 빨간색 또는 녹색 피부를 가진 둥근 과일로 안은 흰색입니다."));
        flashcards.add(new Englishcourse_words_fc("Banana", "Banana Spell b a n a n a. A long curved fruit which is yellow when ripe.", "Banana สะกด b a n a n aกล้วย ผลโค้งยาวเมื่อสุกมีสีเหลือง","Banana 스펠 b a n a n a 바나나, 익을 때 노란색인 길고 구부러진 과일"));
        flashcards.add(new Englishcourse_words_fc("E-icon","E-icon Spell e i c o n. ODA project: Global teams of overseas and Korean students and teachers develop and compete in an App development contest.","E-icon สะกด e i c o n เป็นโครงการ ODA: ทีมสากลที่ประกอบด้วยนักศึกษาและครูต่างประเทศและนักศึกษาและครูของเกาหลีพัฒนาแอปและแข่งขันกัน", "E-icon 스펠 e i c o n \n" +
                "ODA 프로젝트: 해외 학생과 교사로 구성된 글로벌 팀과 한국 학생과 교사들이 앱을 개발하고 경쟁하는 대회입니다."));
        // Add more flashcards with Korean definitions as needed.

        return flashcards;
    }
}
