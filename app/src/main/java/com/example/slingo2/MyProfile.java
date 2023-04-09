package com.example.slingo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfile extends AppCompatActivity {
    private TextView textViewWelcome,textViewFullName,textViewEmail,textViewMobile,textViewGender;

    private String fullName,email,mobile,gender;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        textViewWelcome=findViewById(R.id.displayNameProfileSection);
        textViewFullName=findViewById(R.id.textview_show_full_name);
        textViewEmail=findViewById(R.id.textview_show_email);
        textViewMobile=findViewById(R.id.textview_show_phone);
        textViewGender=findViewById(R.id.textview_show_gender);
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        if(firebaseUser==null){
            Toast.makeText(this, "Something went wrong, user data is not available", Toast.LENGTH_SHORT).show();
        }
        else{

            showUserProfile(firebaseUser);
        }
    }
    private void showUserProfile(FirebaseUser firebaseUser){
        String uID=firebaseUser.getUid();
        //extract data from database
        DatabaseReference refrencePath= FirebaseDatabase.getInstance().getReference("UserData");
        refrencePath.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData readUserData=snapshot.getValue(UserData.class);
                if(readUserData!=null){
                    fullName=readUserData.fullName;
                    email=readUserData.email;
                    gender=readUserData.Gender;
                    mobile=readUserData.Phone;

                    textViewWelcome.setText("Welcome "+fullName+"!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }
}