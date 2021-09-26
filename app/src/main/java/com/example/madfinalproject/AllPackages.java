package com.example.madfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AllPackages extends AppCompatActivity {

    ImageView imageBack,addPkgBtn;
    TextView actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_packages);

        //action bar
        actionBar = findViewById(R.id.actionBar);
        actionBar.setText("All Package");

        imageBack= findViewById(R.id.imageBack);

        addPkgBtn = findViewById(R.id.addPkgBtn);

        //back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllPackages.super.onBackPressed();
            }
        });

        //Add button
        addPkgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllPackages.this,AddPackage.class);
                startActivity(intent);
            }
        });

    }
}