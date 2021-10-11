package com.example.mltextreader;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;



public class SpeechToTextActivity extends AppCompatActivity {

    String url;


    private TextView txvResult;
    private String SpokeWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        txvResult = findViewById(R.id.txvResult);


        txvResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLongClick(false);
            }
        });

        //checking for value in the word space when rotated and if there was one put it in again
        if (savedInstanceState != null) {
            SpokeWord = savedInstanceState.getString("word");
            txvResult.setText(SpokeWord);
        }
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Does not support voice input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));
                }
                break;
        }
    }

    private boolean onLongClick (boolean v)//This opens up the xml activity for popup to display word definition and allow user to save word
    {
        //below is calling the method in the dictionary request class to get the definition
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.description_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);


        DictionaryRequest dR = new DictionaryRequest(this, txvResult, popupView, popupWindow);
        url = dictionaryEntries();
        dR.execute(url);


        return false;

    }
    private String dictionaryEntries() {

        final String language = "en-gb";
    final String word = txvResult.getText().toString();
    final String fields = "definitions";
    final String strictMatch = "false";
    final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
}

    // saves the word in text box for screen rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("word", SpokeWord);
    }


}