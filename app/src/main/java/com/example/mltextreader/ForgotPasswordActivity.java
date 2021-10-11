package com.example.mltextreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgotPasswordEmail;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPasswordEmail = (EditText) findViewById(R.id.txtEmailAddressForgotPassword);
        resetPasswordButton = (Button) findViewById(R.id.btnResetPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String email = forgotPasswordEmail.getText().toString().trim();

        if (email.isEmpty()) {
            forgotPasswordEmail.setError("Email is needed!");
            forgotPasswordEmail.requestFocus();
            return;
        }



        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this,"Checkyour emails to reset your password",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this,"Try again, Something wasnt Right!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goLogin(View view){
        startActivity(new Intent(ForgotPasswordActivity.this, loginActivity.class));
    }


}