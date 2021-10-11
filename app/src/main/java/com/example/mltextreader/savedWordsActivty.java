package com.example.mltextreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class savedWordsActivty extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseUser mCurrentUser;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_words);
        db = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = mCurrentUser.getUid();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Worddef> savedWord = new ArrayList<>();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(savedWord);
        recyclerView.setAdapter(adapter);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading saved word. Please wait...");
        progressDialog.show();

        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        Log.d("words", documentSnapshot.getData().toString());
                        Map<String, Object> map = documentSnapshot.getData();

                        for(Map.Entry<String, Object> entry : map.entrySet()) {
                            Log.d("words", entry.getKey()+entry.getValue());
                            Worddef worddef = new Worddef(entry.getValue().toString(), entry.getKey());
                            savedWord.add(worddef);
                            adapter.notifyDataSetChanged();
                            progressDialog.cancel();
                        }
                    }
                } else {
                    Toast.makeText(savedWordsActivty.this, "an error occured", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(savedWordsActivty.this, "can't load words", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


    }
}