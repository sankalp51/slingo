package com.example.slingo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordInside extends AppCompatActivity {
    private EditText email;
    private AppCompatButton reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_inside);
        email=findViewById(R.id.Email_reset_password);
        reset=findViewById(R.id.reset_password2);

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
                            Toast.makeText(ForgotPasswordInside.this, "Check the verification email sent to "+email.getText().toString(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordInside.this,MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(ForgotPasswordInside.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}