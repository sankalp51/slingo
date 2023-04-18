package com.example.slingo2;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

public class Setttings extends Fragment {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String[] items = {"Edit Profile", "Update Password", "Log out"};
    FirebaseAuth auth;
    public static final String SHARED_PREFS="SharedPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setttings, container, false);
        listView = view.findViewById(R.id.settings_list);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    startActivity(new Intent(getContext(),UpdateProfile.class));

                }
                else if(i==1){
                    startActivity(new Intent(getContext(),ForgotPasswordInside.class));
                }
                else if(i==2){
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("Are your sure you want to log out?")
                                    .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(getContext(),login.class));
                                                    Activity activity=getActivity();
                                                    activity.finish();
                                                    SharedPreferences sharedPreferences=activity.getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                                    SharedPreferences.Editor editor=sharedPreferences.edit();

                                                    editor.putString("name","");
                                                    editor.apply();
                                                }
                                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();



                }

            }
        });

        return view;
    }
}