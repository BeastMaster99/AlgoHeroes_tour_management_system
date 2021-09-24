package com.example.madfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HotelTravelerMainView extends AppCompatActivity {
    String hotelId;

    TextView actionBar;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_traveler_main_view);

        Intent intent = getIntent();
        hotelId = intent.getStringExtra("hotelId");

        imageBack = findViewById(R.id.imageBack);

        //setting the onClick listener for the back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelTravelerMainView.super.onBackPressed();
            }
        });

        actionBar = findViewById(R.id.actionBar);
        actionBar.setText(R.string.hotel_detail_title);

    }
}