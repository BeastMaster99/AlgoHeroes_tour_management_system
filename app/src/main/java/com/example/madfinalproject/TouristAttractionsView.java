package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.HashMap;


public class TouristAttractionsView extends AppCompatActivity {
    String placeId;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    TextView actionBar, placeName, placeAddress, placeCity, placeDescription;
    ImageView imageBack;
    SliderView sliderView;

    AttractionPlaces attractionPlaces = new AttractionPlaces();

    HashMap<String, String> images = new HashMap<>();

    DatabaseReference placesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_attractions_view);

        Intent intent = getIntent();
        placeId = intent.getStringExtra("placeId");
        System.out.println(placeId);
        imageBack = findViewById(R.id.imageBack);

        //setting the onClick listener for the back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TouristAttractionsView.super.onBackPressed();
            }
        });

        actionBar = findViewById(R.id.actionBar);
        actionBar.setText(R.string.attractionDetails);

        placeName = findViewById(R.id.placeName);
        placeAddress = findViewById(R.id.placeAddress);
        placeCity = findViewById(R.id.placeCity);
        placeDescription = findViewById(R.id.placeDescription);

        sliderView = findViewById(R.id.imageSlider);
        //instantiating the slider adapter
        placeImageSliderAdapter adapter = new placeImageSliderAdapter(this);


        //setting the slider view
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        //place database Ref
        placesRef = databaseReference.child("Places").child(placeId);

        //getting the places in the database
        placesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    attractionPlaces = snapshot.getValue(AttractionPlaces.class);

                    assert attractionPlaces != null;
                    adapter.setInputUrls(attractionPlaces.getImages());
                    adapter.notifyDataSetChanged();

                    placeName.setText(attractionPlaces.getName());
                    placeAddress.setText(attractionPlaces.getAddress());
                    placeCity.setText(attractionPlaces.getCity());
                    placeDescription.setText(attractionPlaces.getDescription());

                    images = attractionPlaces.getImages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TouristAttractionsView.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}