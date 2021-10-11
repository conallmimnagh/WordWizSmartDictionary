package com.example.mltextreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
//import android.support.v4.media.MediaBrowserCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;
import android.os.AsyncTask;


public class TextReaderActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    String url;
    private  Button snapBtn, detectBtn, exitpopupBtn, savewordBtn, savedBtn;
    private ImageView imageView;
    private TextView textView, wordTxt, definitionTxt;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitMap;
    private  Button speechBtn;

    private TextToSpeech tts;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Worddef worddef;
    private String mFirebaseUser, ExtractedWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reader);

        snapBtn = findViewById(R.id.snapBtn);
        detectBtn = findViewById(R.id.detectBtn);
        speechBtn = findViewById(R.id.speechBtn);
        savedBtn = findViewById(R.id.savedBtn);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        definitionTxt = findViewById(R.id.definitionTxt);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

        String ExtractedWord = textView.getText().toString();
        worddef = new Worddef();

        tts = new TextToSpeech(this,this);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLongClick(false);

            }
        });


        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }

        });

        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTxt();
            }
        });

        savedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TextReaderActivity.this, savedWordsActivty.class);
                startActivity(intent);
                //Toast.makeText(TextReaderActivity.this, "save page clicked", Toast.LENGTH_LONG).show();
            }
        });

        //Below is the text to speech for text box output

        speechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });

        //checking for value in the word space when rotated and if there was one put it in again
        if (savedInstanceState != null) {
            ExtractedWord = savedInstanceState.getString("ExtractedWord");
            textView.setText(ExtractedWord);
        }

    }//end onCreate
        @Override
        public void onDestroy() {
        //shuting down tts
            if (tts != null) {
                tts.stop();
                tts.shutdown();
            }
            super.onDestroy();
        }

        @Override
        public void onInit(int status) {

            if (status == TextToSpeech.SUCCESS) {

                int result = tts.setLanguage(Locale.ENGLISH);

                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "This Language is not supported");
                } else {
                    speechBtn.setEnabled(true);
                    speakOut();
                }

            } else {
                Log.e("TTS", "Initilization Failed!");
            }

        }

        private void speakOut() {
            String text = textView.getText().toString();
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }




    private void getValues(){
        worddef.setWord(textView.getText().toString());
        worddef.setDef(definitionTxt.getText().toString());
    }


    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitMap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitMap);

        }
    }

    private void detectTxt(){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitMap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void processTxt(FirebaseVisionText text){
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();
        if (blocks.size() == 0 ){
            Toast.makeText(this,"No Text...", Toast.LENGTH_SHORT).show();
            return;
        }
        for (FirebaseVisionText.TextBlock block : text.getTextBlocks()){
            String txt = block.getText();
            textView.setText(txt);
        }
    }




    private boolean onLongClick (boolean v)//This opens up the xml activity for popup to display word definition and allow user to save word
    {

        String ExtractedWord = textView.getText().toString();

    if(!validateExtractedWord(ExtractedWord)){
            Toast.makeText(this, "Please only extract letters from your image", Toast.LENGTH_LONG).show();
            return true;
        }

        //below is calling the method in the dictionary request class to get the definition
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.description_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        /*pass the popupview and popupwindow ui thread to asynctask as parameter.*/

        DictionaryRequest dR = new DictionaryRequest(this, textView, popupView, popupWindow);
        url = dictionaryEntries();
        dR.execute(url);

        return false;

    }

    private String dictionaryEntries() {
        final String language = "en-gb";
        final String word = textView.getText().toString();
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("ExtractedWord", ExtractedWord);
    }

    public boolean validateExtractedWord(String ExtractedWord){

        //password must contain a number, special char, captical letter and be more than 5 characters
        String wordVal = "^[a-zA-Z]+$";

        if (ExtractedWord.length() < 1) {
            //   Toast.makeText(RegisterActivity.this, "extracted word must be more than one character", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ExtractedWord.matches(wordVal)) {
            // Toast.makeText(RegisterActivity.this, "Extracted word can only contain letters", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}//end activity  TextReaderActivity
