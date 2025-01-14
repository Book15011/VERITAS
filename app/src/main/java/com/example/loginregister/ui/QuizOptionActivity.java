package com.example.loginregister.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.loginregister.R;
import com.example.loginregister.ui.page.MathQuizActivity;
import com.example.loginregister.util.Constants;

public class QuizOptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_option);
        CardView cvMath = findViewById(R.id.cvMath);
        findViewById(R.id.imageViewQuizOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cvMath.setOnClickListener(view -> {
            mathClick();
        });

    }

    public void mathClick() {
        Intent intent = new Intent(QuizOptionActivity.this, MathQuizActivity.class);
        intent.putExtra(Constants.SUBJECT, getString(R.string.geography));
        startActivity(intent);

    }


}