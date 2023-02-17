package com.example.slingo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signUp extends AppCompatActivity {
    private boolean passwordShowing=false;
    private boolean conPasswordShowing=false;
    TextView alreadyHaveAnAccount;
    EditText inputEmail,inputPassword,inputConPassword,fullName,phoneNumber;
    RadioButton radioGenderMale,radioGenderFemale;
    AppCompatButton signUpbtn;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    String Gender="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fullName=findViewById(R.id.fullName);
        alreadyHaveAnAccount=findViewById(R.id.signInBtn);
        inputEmail=findViewById(R.id.emailET);
        phoneNumber=findViewById(R.id.mobile);
        inputPassword=findViewById(R.id.passwordET);
        inputConPassword=findViewById(R.id.conPasswordET);
        radioGenderMale=(RadioButton) findViewById(R.id.Male);
        radioGenderFemale=(RadioButton) findViewById(R.id.Female);
        progressBar=findViewById(R.id.progressBar1);
        signUpbtn=findViewById(R.id.signUpBtn);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("UserData");
        final ImageView passwordIcon=findViewById(R.id.passwordIcon);
        final ImageView conPasswordIcon=findViewById(R.id.conPasswordIcon);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordShowing){
                    passwordShowing=false;
                    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_hide);
                }
                else{
                    passwordShowing=true;
                    inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_show);
                }
                //move the cursor at the end of the password string
                inputPassword.setSelection(inputPassword.length());
            }
        });
        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conPasswordShowing){
                    conPasswordShowing=false;
                    inputConPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.password_hide);
                }
                else{
                    conPasswordShowing=true;
                    inputConPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.password_show);
                }
                //move the cursor at the end of the password string
                inputConPassword.setSelection(inputConPassword.length());
            }
        });

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signUp.this,login.class));
            }
        });
        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                signUpbtn.setVisibility(View.INVISIBLE);
                perforAuth();

            }
        });
    }

    private void perforAuth(){
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        String conPassword=inputConPassword.getText().toString();
        String fName=fullName.getText().toString();
        String mobile=phoneNumber.getText().toString();
        boolean male=radioGenderMale.isChecked();
        boolean female=radioGenderFemale.isChecked();

        if(radioGenderMale.isChecked()){
            Gender="Male";
        }
        if(radioGenderFemale.isChecked()){
            Gender="Female";
        }

        if(fName.isEmpty()||fName.matches("[a-zA-Z]+")){
            fullName.setError("Enter your full name please");
            progressBar.setVisibility(View.GONE);
            signUpbtn.setVisibility(View.VISIBLE);
        }
        else if(!email.matches(emailPattern)){
            inputEmail.setError("Please enter a valid email address");
            progressBar.setVisibility(View.GONE);
            signUpbtn.setVisibility(View.VISIBLE);
        }
        else if(mobile.isEmpty()&&mobile.matches("^[+][0-9]{10,13}$")||mobile.length()>10){
            phoneNumber.setError("Enter a valid mobile number");
            progressBar.setVisibility(View.GONE);
            signUpbtn.setVisibility(View.VISIBLE);
        }
        else if(password.isEmpty()||password.length()<6){
            inputPassword.setError("Password doesn't match with confirm password");
            progressBar.setVisibility(View.GONE);
            signUpbtn.setVisibility(View.VISIBLE);
        }
        else if (!password.equals(conPassword)) {
            inputConPassword.setError("Password is not matching in both the fields");
            progressBar.setVisibility(View.GONE);
            signUpbtn.setVisibility(View.VISIBLE);
        }
        else if(!male && !female){
            Toast.makeText(this, "Please select a gender and continue signup", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            signUpbtn.setVisibility(View.VISIBLE);
        }
        else{
            progressDialog.setMessage("Please wait for Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(signUp.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        UserData data=new UserData(fName,email,Gender,mobile);
                        FirebaseDatabase.getInstance().getReference("UserData").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        perfromPhoneAuth();
                                        Toast.makeText(signUp.this, "Complete the OTP verification to proceed", Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(signUp.this, "Account already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private void perfromPhoneAuth(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                signUp.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        signUpbtn.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        signUpbtn.setVisibility(View.VISIBLE);
                        Toast.makeText(signUp.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        signUpbtn.setVisibility(View.VISIBLE);
                        Intent intent=new Intent(signUp.this,OTPverification.class);
                        intent.putExtra("otp",phoneNumber.getText().toString());
                        intent.putExtra("verificationID",s);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }
        );
    }


}