package com.example.loginregister;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class Scholar_inter_nation_link extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_englishcourse_alphabets);

        webView = findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Load the YouTube video URL
        final String youtubeVideoUrl = "https://globalscholarships.com/opencourses/";

        // Load URL on UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(youtubeVideoUrl);
            }
        });
    }
}