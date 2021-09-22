package com.example.madfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button getStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating session hotel owner object and validating the login
        SessionsHotelOwner sessionsHotelOwner = new SessionsHotelOwner(MainActivity.this);
        sessionsHotelOwner.checkHotelOwnerLogin();

        //creating session tour guide object and validating the login
        SessionsTourGuide sessionsTourGuide = new SessionsTourGuide(MainActivity.this);
        sessionsTourGuide.checkTourGuideLogin();

        //creating session traveler object and validating the login
        SessionsTraveler sessionsTraveler = new SessionsTraveler(MainActivity.this);
        sessionsTraveler.checkTravelerGuideLogin();

        getStart = (Button) findViewById(R.id.landButton);
        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            //This onClick listener is called when user taps Get Started
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
}