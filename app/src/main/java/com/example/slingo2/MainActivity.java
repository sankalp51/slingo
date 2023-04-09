package com.example.slingo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS="SharedPrefs";
    TextView text1;
    Button go,interprete;
    BottomNavigationView bottomNavigationView;
    Home home=new Home();
    Setttings setttings=new Setttings();
    Profile profile=new Profile();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.botton_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profile).commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,setttings).commit();
                        return true;
                }
                return false;
            }
        });
//        go=findViewById(R.id.Btn);
//        interprete=findViewById(R.id.interpreter);
//        text1=findViewById(R.id.profileActivity);
//        go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//
//                editor.putString("name","");
//                editor.apply();
//                startActivity(new Intent(MainActivity.this,login.class));
//                finish();
//            }
//        });
//        interprete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,SignInterpreter.class));
//            }
//        });
//        text1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,MyProfile.class));
//            }
//        });
    }
}