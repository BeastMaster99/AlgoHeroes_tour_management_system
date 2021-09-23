package com.example.madfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AddHotel extends AppCompatActivity {
    TextView title;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);

        title = findViewById(R.id.actionBar);
        imageBack = findViewById(R.id.imageBack);

        title.setText(R.string.hotel_activity_topic);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHotel.super.onBackPressed();
            }
        });
    }
}