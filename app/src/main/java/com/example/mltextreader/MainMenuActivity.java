package com.example.mltextreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    public void goTextReader(View view) {
        startActivity(new Intent(MainMenuActivity.this,TextReaderActivity.class));
    }

    public void goSpeechToText(View view) {
        startActivity(new Intent(MainMenuActivity.this,SpeechToTextActivity.class));
    }

    public void goSavedWords(View view) {
        startActivity(new Intent(MainMenuActivity.this,savedWordsActivty.class));
    }

}

