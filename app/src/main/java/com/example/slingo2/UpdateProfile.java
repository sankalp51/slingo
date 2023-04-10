package com.example.slingo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {
    private EditText editTextUpdateName,editTextUpdatePhone,editTextUpdateEmail;
    private String fullName,gender,phone,email;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        progressBar=findViewById(R.id.updateProfilePicBar);
        editTextUpdateName=findViewById(R.id.editText_new_name);
        editTextUpdatePhone=findViewById(R.id.editText_new_phone);
        editTextUpdateEmail=findViewById(R.id.editText_new_email);
        radioGroupUpdateGender=findViewById(R.id.genderRadioGroupUpdate);
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        Button newProfilePicture=findViewById(R.id.updateNewProfilePicture);
        Button updateProfileBtn=findViewById(R.id.updateProfileInfo);
        showProfile(firebaseUser);
        newProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProfile.this,UploadProfilePicture.class));
            }
        });
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               updateProfile(firebaseUser);
            }
        });

    }

    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderId=radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGenderSelected=findViewById(selectedGenderId);
        fullName=editTextUpdateName.getText().toString();
        email=editTextUpdateEmail.getText().toString();
        phone=editTextUpdatePhone.getText().toString();
        if(fullName.isEmpty()||fullName.matches("[a-zA-Z]+")){
            editTextUpdateName.setError("Enter your name please");
            editTextUpdateName.requestFocus();
            progressBar.setVisibility(View.GONE);
        }
        else if(phone.length() != 10){
            editTextUpdatePhone.setError("Enter a valid phone number");
            editTextUpdatePhone.requestFocus();
            progressBar.setVisibility(View.GONE);
        } else if (!email.matches(emailPattern)) {
            editTextUpdateEmail.setError("Enter a valid email address");
            editTextUpdateEmail.requestFocus();
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(radioButtonUpdateGenderSelected.getText())){
            Toast.makeText(this, "Gender is required", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else{
            gender=radioButtonUpdateGenderSelected.getText().toString();
            UserData WriteUserData=new UserData(fullName,email,gender,phone);
            DatabaseReference refrenceProfile=FirebaseDatabase.getInstance().getReference("UserData");
            String userId=firebaseUser.getUid();
            progressBar.setVisibility(View.VISIBLE);
            refrenceProfile.child(userId).setValue(WriteUserData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(UpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(UpdateProfile.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        try{
                            throw task.getException();
                        }
                        catch (Exception e){
                            Toast.makeText(UpdateProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userId=firebaseUser.getUid();
        DatabaseReference refrenceProfile=FirebaseDatabase.getInstance().getReference("UserData");
        progressBar.setVisibility(View.VISIBLE);
        refrenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData readWriteUserDetails=snapshot.getValue(UserData.class);
                if(readWriteUserDetails!=null){
                    fullName=readWriteUserDetails.fullName;
                    gender=readWriteUserDetails.Gender;
                    email=readWriteUserDetails.email;
                    phone=readWriteUserDetails.Phone;

                    editTextUpdateName.setText(fullName);
                    editTextUpdateEmail.setText(email);
                    editTextUpdatePhone.setText(phone);

                    if(gender.equals("Male")){
                        radioButtonUpdateGenderSelected=findViewById(R.id.newMale);
                    }
                    else{
                        radioButtonUpdateGenderSelected=findViewById(R.id.newFemale);
                    }
                    radioButtonUpdateGenderSelected.setChecked(true);
                }
                else{
                    Toast.makeText(UpdateProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}