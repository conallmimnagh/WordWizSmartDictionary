package com.example.mltextreader;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing firebase Auth object
        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar_UsrReg);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void registerUser(View view){
        //getting email and password form edit texts
        String email = ((EditText) findViewById(R.id.editText_UserEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_Password)).getText().toString();

        //checking if email and password are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        //if the email and password are not empty displaying a progress dialog
        progressBar.setVisibility(View.VISIBLE);
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    //display message
                    Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, loginActivity.class));
                    finish();
                }else{
                    //display message
                    FirebaseAuthException e = (FirebaseAuthException)task.getException();
                    Toast.makeText(RegisterActivity.this, "Failed registration: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goLogin(View view) {
        startActivity(new Intent(RegisterActivity.this, loginActivity.class));
    }
}
