package com.example.slingo2;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Profile extends Fragment {
    public static final String SHARED_PREFS="SharedPrefs";
    private TextView textViewWelcome,textViewFullName,textViewEmail,textViewMobile,textViewGender;

    private String fullName,email,mobile,gender;
    private FirebaseAuth authProfile;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewWelcome=view.findViewById(R.id.displayNameProfileSection);
        textViewFullName=view.findViewById(R.id.textview_show_full_name);
        textViewEmail=view.findViewById(R.id.textview_show_email);
        textViewMobile=view.findViewById(R.id.textview_show_phone);
        textViewGender=view.findViewById(R.id.textview_show_gender);
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        if(firebaseUser==null){
            Toast.makeText(getActivity(), "Something went wrong, user data is not available", Toast.LENGTH_SHORT).show();
        }
        else{
            showUserProfile(firebaseUser);
        }

        return view;
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
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
