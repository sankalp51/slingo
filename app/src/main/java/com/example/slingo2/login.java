package com.example.slingo2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class login extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    AppCompatButton LogIn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private boolean passwordShowing=true;
    TextView createNewAccount;
    TextView resetPassword;

    public static final String SHARED_PREFS="SharedPrefs";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        inputPassword = findViewById(R.id.passwordET);
        createNewAccount = findViewById(R.id.signupBtn);
        inputEmail = findViewById(R.id.Email);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        LogIn = findViewById(R.id.signinBtn);
        resetPassword=findViewById(R.id.forgotPasswordBtn);

        checkBox();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,forgetPassword.class));
                finish();
            }
        });


        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogIn();
            }
        });


        passwordIcon.setOnClickListener(new View.OnClickListener() {
            //showing if password is showing or not
            @Override
            public void onClick(View view) {
                if (passwordShowing) {
                    passwordShowing = false;
                    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_hide);
                } else {
                    passwordShowing = true;
                    inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_show);
                }
                //move the cursor at the end of the password string
                inputPassword.setSelection(inputPassword.length());
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, signUp.class));
            }
        });


    }

    private void checkBox() {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String check=sharedPreferences.getString("name","");
        if(check.equals("true")){
            sendUserToNextActivity();
            finish();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                sendUserToNextActivity();
                finish();
            } catch (ApiException e) {
                Toast.makeText(this, "Couldn't sign in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void performLogIn() {
        String email = inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        if (!email.matches(emailPattern)) {
            inputEmail.setError("Please enter a valid email address");
            inputEmail.requestFocus();
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Please enter the correct password");
            inputPassword.requestFocus();
        } else {
            progressDialog.setMessage("Please wait while login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();

                        editor.putString("name","true");
                        editor.apply();
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(login.this, "login successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(login.this, "Account doesn't exist, create a new account", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(login.this,signUp.class));
                        finish();
                    }
                }
            });

        }

    }
    private void sendUserToNextActivity(){
        Intent intent=new Intent(login.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}