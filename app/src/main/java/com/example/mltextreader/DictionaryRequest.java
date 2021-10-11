package com.example.mltextreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DictionaryRequest extends AsyncTask <String, Integer, String> {

    Context context;
    TextView textView;

    View popupView;
    PopupWindow popupWindow;

    private Intent intent;
    String defText;
    FirebaseUser mCurrentUser;
    FirebaseFirestore db;

    String uid;

    DictionaryRequest(Context context, TextView tV, View popupView, PopupWindow popupWindow) {
        this.context = context;
        textView = tV;
        this.popupView = popupView;
        this.popupWindow = popupWindow;
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        uid = mCurrentUser.getUid();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("app_id", app_id);
            urlConnection.setRequestProperty("app_key", app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            Log.d("jsonResult", stringBuilder.toString());
            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();

            return e.toString();
        }
    }

    public void onPostExecute(String result) {

        super.onPostExecute(result);



        try {

            JSONObject js = new JSONObject(result);
            JSONArray results = js.getJSONArray("results");

            JSONObject lEntries = results.getJSONObject(0);
            JSONArray laArray = lEntries.getJSONArray("lexicalEntries");

            JSONObject entries = laArray.getJSONObject(0);
            JSONArray e = entries.getJSONArray("entries");

            JSONObject jsonObject = e.getJSONObject(0);
            JSONArray sensesArray = jsonObject.getJSONArray("senses");

            JSONObject de = sensesArray.getJSONObject(0);
            JSONArray d = de.getJSONArray("definitions");

            String def = d.getString(0);
            defText = def;
            Log.d("test", defText);

            /* Used to make popup*/
            TextView wordTxt = (TextView) popupView.findViewById(R.id.wordTxt);
            TextView definitionTxt = (TextView) popupView.findViewById(R.id.definitionTxt);
            Button saveWordBtn = (Button) popupView.findViewById(R.id.savewordBtn);



            wordTxt.requestFocus();
            wordTxt.setText(textView.getText().toString());

            definitionTxt.setText(def);

            popupWindow.showAtLocation(popupView, Gravity.CENTER,5,5);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            popupWindow.update();

            saveWordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Worddef worddef = new Worddef(def, textView.getText().toString());
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("saving data! please wait...");
                    progressDialog.show();

                    Map<String, String> map = new HashMap<>();
                    map.put(textView.getText().toString(), def);

                    db.collection("users").document(uid).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "saved", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "can't save", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }//end on postexcute
}

