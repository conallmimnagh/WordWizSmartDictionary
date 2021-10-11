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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


public class TextReaderActivity extends AppCompatActivity{

    String url;
    private  Button snapBtn, detectBtn, exitpopupBtn, savewordBtn;
    private ImageView imageView;
    private TextView textView, wordTxt, definitionTxt;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitMap;
    private  Button speechBtn;
    TextToSpeech t1;

    //add firebase objects
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference ref;
    Worddef worddef;
    private String mFirebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reader);

        snapBtn = findViewById(R.id.snapBtn);
        detectBtn = findViewById(R.id.detectBtn);
        speechBtn = findViewById(R.id.speechBtn);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        definitionTxt = findViewById(R.id.definitionTxt);

//declare database reference object below, This is how we access database
        //You must be signed in for this to work

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();

        ref = database.getInstance().getReference().child("worddef");
        worddef = new Worddef();


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

        //Below is the text to speech for text box output
        t1 = new TextToSpeech(getApplicationContext(),
            new TextToSpeech.OnInitListener() {
                String text = textView.getText().toString();

                @Override
                public void onInit(int status) {
                    if (status==TextToSpeech.SUCCESS)

                    {
                        int result = t1.setLanguage(Locale.UK);

                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                        {
                            Log.e("TTS", "Language not supported");
                        }else{
                            speechBtn.setEnabled(true);
                        }
                    }else{
                        Log.e("TTS", "Initialization failed");
                    }
                }
            });


        speechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

    }//end OnCreate


    private void getValues(){
        worddef.setWord(textView.getText().toString());
        worddef.setDef(definitionTxt.getText().toString());
    }

    public void savewordBtn(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                getValues();
                ref.child(mFirebaseUser).setValue(worddef);
                Toast.makeText(TextReaderActivity.this,"Data inserted .. ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void speak(){
            String text = textView.getText().toString();

            t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy(){
            if (t1 != null) {
                t1.stop();
                t1.shutdown();
            }
            super.onDestroy();
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

    public void onPause() //used for pausing text speech reading
    {
        if (t1!=null)
        {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();

    }


    private boolean onLongClick (boolean v)//This opens up the xml activity for popup to display word definition and allow user to save word
    {
        //below is calling the method in the dictionary request class to get the definition
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.description_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        /*pass the popupview and popupwindow ui thread to asynctask as parameter.
        as we cant create layout on asynctask. thats why we are creating layout here
        * and then pass it as a parameter*/

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


}//end activity  TextReaderActivity
