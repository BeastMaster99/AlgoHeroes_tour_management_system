package com.example.madfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    TextView signIn;
    Button traveler;
    Button hotelOwner;
    Button tourGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signIn = (TextView) findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            //This onClick listener called when user taps Sign In
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });

        traveler = (Button) findViewById(R.id.buttonTraveler);
        traveler.setOnClickListener(new View.OnClickListener() {
            @Override
            //This onClick listener called when user taps Sign Up as a Traveler
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,TravelerRegistration.class);
                startActivity(intent);
            }
        });

        hotelOwner = (Button) findViewById(R.id.buttonHotelOwner);
        hotelOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            //This onClick listener called when user taps Sign Up as a Hotel Owner
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,HotelOwnerRegistration.class);
                startActivity(intent);
            }
        });

        tourGuide = (Button) findViewById(R.id.buttonTourGuide);
        tourGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            //This onClick listener called when user taps Sign Up as a Tour Guide
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,TourGuideRegistration.class);
                startActivity(intent);
            }
        });
    }
}