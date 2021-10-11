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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        //getting email and password from edit texts
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
        if(!validateEmail(email)){
            Toast.makeText(this, "Please enter correct email format", Toast.LENGTH_LONG).show();
            return;
        }
        if(!validatePassword(password)){
            Toast.makeText(this, "Please enter password with capital, special character and number", Toast.LENGTH_LONG).show();
            return;
        }
        //if the email and password are not empty displaying a progress dialog
        {
            progressBar.setVisibility(View.VISIBLE);
        }

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

    //checking for valid email entered
    public boolean validateEmail(String email) {
        String correctEmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";

        if (email.isEmpty()) {
            //Toast.makeText(RegisterActivity.this, "email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.matches(correctEmailPattern)) {
            //Toast.makeText(RegisterActivity.this, "incorrect email format", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // checking for strong password
    public boolean validatePassword(String password){

        //password must contain a number, special char, captical letter and be more than 5 characters
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //must have 1 number
                "(?=.*[A-Z])" +         //must have 1 uppercase
                "(?=.*[@#$%^*?&+=])" +    //must have 1 special char
                ".{0,}$";

        if (password.length() < 5) {
         //   Toast.makeText(RegisterActivity.this, "password cannot be less than 5 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.matches(passwordVal)) {
           // Toast.makeText(RegisterActivity.this, "password needs a capital, number and special character", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}




