package com.example.mltextreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginActivity extends AppCompatActivity {
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(View view) {
        String email = ((EditText) findViewById(R.id.editText_Log_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_Log_password)).getText().toString();

        //checking if email and password are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }


        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if task success
                if (task.isSuccessful()) {
                    Toast.makeText(loginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                    //start the profile activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                } else {
                    //display message
                    Toast.makeText(loginActivity.this, "Login Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goRegister(View view) {
        startActivity(new Intent(loginActivity.this, RegisterActivity.class));
    }

    public void goForogotPassword(View view) {
        startActivity(new Intent(loginActivity.this, ForgotPasswordActivity.class));
    }
}




