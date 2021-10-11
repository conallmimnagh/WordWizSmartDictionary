package com.example.mltextreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

    DictionaryRequest(Context context, TextView tV, View popupView, PopupWindow popupWindow) {
        this.context = context;
        textView = tV;
        this.popupView = popupView;
        this.popupWindow = popupWindow;
    }

    @Override
    protected String doInBackground(String... params) {

        /*Below is the app id and app key for the oxford dictionary api*/
        final String app_id = "7a23ad7e";
        final String app_key = "a8a2bbe9e0b1fa18eb7a6ae9f6284f49";
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

        String def = "";

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

            def = d.getString(0);
            defText = def;
            Log.d("test", defText);

            /* here we are showing the definition as popup*/
            TextView wordTxt = (TextView) popupView.findViewById(R.id.wordTxt);
            TextView definitionTxt = (TextView) popupView.findViewById(R.id.definitionTxt);
            Button exitpopupBtn = (Button)popupView.findViewById(R.id.exitpopupBtn);

            wordTxt.requestFocus();
            wordTxt.setText(textView.getText().toString());

            definitionTxt.setText(def);

            popupWindow.showAtLocation(popupView, Gravity.CENTER,5,5);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            popupWindow.update();
            exitpopupBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    textView.setText(wordTxt.getText().toString());
                    popupWindow.dismiss();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }//end on postexcute
}

