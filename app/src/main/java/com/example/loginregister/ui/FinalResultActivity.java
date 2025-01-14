package com.example.loginregister.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginregister.Home;
import com.example.loginregister.R;
import com.example.loginregister.local_data.QuizPref;
import com.example.loginregister.model.HistoryModel;
import com.example.loginregister.util.Constants;
import com.example.loginregister.util.Utils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import nl.dionsegijn.konfetti.xml.KonfettiView;

public class FinalResultActivity extends AppCompatActivity {
    private TextView tvSubject, tvCorrect, tvIncorrect, tvDate, tvWellDone;
    private QuizPref quizPref;
    private FirebaseFirestore db;
    HistoryModel historyModel;
    KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);
        initView();
        onBackPressed();
        Intent intent = getIntent();
        quizPref = new QuizPref(this);
        int correctAnswer = intent.getIntExtra(Constants.CORRECT, 0);
        int incorrectAnswer = intent.getIntExtra(Constants.INCORRECT, 0);
        String subject = intent.getStringExtra(Constants.SUBJECT);
        int earnedPoints = (correctAnswer * Constants.CORRECT_POINT) - (incorrectAnswer * Constants.INCORRECT_POINT);
        TextView textView=findViewById(R.id.result_);

        historyModel = new HistoryModel(Calendar.getInstance().getTimeInMillis(), subject, correctAnswer, incorrectAnswer, earnedPoints);
        if (historyModel.getEarned() < 0) {
            historyModel.setEarned(0);
        }
        if (historyModel.getOverallPoints() < 0) {
            historyModel.setOverallPoints(0);

        }
        historyModel.setOverallPoints(quizPref.getPoint() + historyModel.getEarned());
        quizPref.savePoint(quizPref.getPoint() + historyModel.getEarned());
        if (!intent.getStringExtra(Constants.TYPE).equals("history")) {
            saveSharedLocalData();
            textView.setText("Final Result");

        }else {
            textView.setText("History Result");
        }
        db = FirebaseFirestore.getInstance();
        displayData(historyModel);
        finishAgain();
    }

    public void finishAgain() {
        findViewById(R.id.btnFinishQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalResultActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void saveSharedLocalData() {
        Type type = new TypeToken<ArrayList<HistoryModel>>() {
        }.getType();
        ArrayList<HistoryModel> historyModelArrayList;
        Gson gson = new Gson();
        if (quizPref.getHistoryQuiz().isEmpty()) {
            historyModelArrayList = new ArrayList<>();
        } else {
            historyModelArrayList = new ArrayList<>(gson.fromJson(quizPref.getHistoryQuiz(), type));
        }
        historyModelArrayList.add(historyModel);
        String toJson = gson.toJson(historyModelArrayList, type);
        quizPref.historyQuiz(toJson);
    }

    public void initView() {
//        konfettiView = findViewById(R.id.viewKonfetti);
        tvSubject = findViewById(R.id.textView16);
        tvCorrect = findViewById(R.id.textView19);
        tvIncorrect = findViewById(R.id.textView27);
        tvDate = findViewById(R.id.textView30);
        tvWellDone = findViewById(R.id.tvWellDone);
    }

    private void displayData(HistoryModel attempt) {
//        final int containerMiddleX = konfettiView.getWidth() / 2;
//        final int containerMiddleY = konfettiView.getHeight() / 2;
//        final ConfettiSource confettiSource = new ConfettiSource(containerMiddleX, containerMiddleY);


        tvSubject.setText(attempt.getSubject());
        tvCorrect.setText(String.valueOf(attempt.getCorrect()));
        tvIncorrect.setText(String.valueOf(attempt.getIncorrect()));
        if (attempt.getCorrect() > 5) {


            tvWellDone.setText("You done good job!," + quizPref.getName());
        } else {
            tvWellDone.setText("Nice Try!" + quizPref.getName());

        }
        tvDate.setText(Utils.formatDate(attempt.getCreatedTime()));
        addScoreData("book@gmail.com");
    }

    private void addScoreData(String email) {
        // Create a new map to store subject scores
        Map<String, Object> subjectScores = new HashMap<>();
        subjectScores.put(tvSubject.getText().toString(), Double.parseDouble(tvCorrect.getText().toString()));
//        subjectScores.put("correct", Integer.parseInt(tvCorrect.getText().toString()));
//        subjectScores.put("incorrect", Integer.parseInt(tvIncorrect.getText().toString()));

        // Add data to Firebase
        db.collection("user_score")
                .document(email)
                .update(subjectScores)
                .addOnSuccessListener(aVoid -> {
                    // Data added successfully
                })
                .addOnFailureListener(e -> {
                    // Error adding data
                });
    }

    public void onBackPressed() {
        findViewById(R.id.imageViewFinalResultQuiz).setOnClickListener(view -> {
            Intent intent = new Intent(FinalResultActivity.this, QuizOptionActivity.class);
            startActivity(intent);
            finish();
        });
    }
}