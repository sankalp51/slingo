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

public class forgetPassword extends AppCompatActivity {
    private TextView nSignIn;
    private EditText email;
    private AppCompatButton reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        nSignIn=findViewById(R.id.signInForget);
        email=findViewById(R.id.Email_reset);
        reset=findViewById(R.id.reset_password);

        nSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgetPassword.this,login.class));
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valid= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String Email=email.getText().toString();
                if(!Email.matches(valid)){
                    email.setError("Please enter a valid email");
                    email.requestFocus();
                }
                else{
                    password();



                }
            }
        });

    }
    private void password(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(forgetPassword.this, "Check the verification email sent to "+email.getText().toString(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(forgetPassword.this,login.class));
                            finish();
                        }
                        else{
                            Toast.makeText(forgetPassword.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}