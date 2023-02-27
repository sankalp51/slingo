package com.example.slingo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {
    TextView LogIn;
    EditText email = (EditText) findViewById(R.id.Email_reset);

    String Email_string = email.getText().toString();

    AppCompatButton reset;

    private FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        LogIn = findViewById(R.id.signInForget);
        reset = findViewById(R.id.reset_password);
        auth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateEmail();
            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordReset.this, login.class));
            }
        });
    }

    private void validateEmail() {

        if (!Email_string.matches(emailPattern)) {
            email.setError("Please enter a valid email address");
        } else {
            forgetPass();
        }
    }


    private void  forgetPass(){
        auth.sendPasswordResetEmail(Email_string)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PasswordReset.this, "An email has been sent to you", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PasswordReset.this, login.class));
                        } else {
                            Toast.makeText(PasswordReset.this, "Some error occurred," + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}