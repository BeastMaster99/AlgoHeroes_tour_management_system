package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.HashMap;
import java.util.Map;


public class tourguideSingleView extends AppCompatActivity {
    String placeId;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    TextView actionBar, placeName, placeAddress, placeCity, placeDescription;
    ImageView imageBack;
    SliderView sliderView;

    Button editPlaceBtn, deletePlaceBtn;


    AttractionPlaces attractionPlaces = new AttractionPlaces();

    HashMap<String, String> images = new HashMap<>();

    DatabaseReference placesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourguide_single_view);

        Intent intent = getIntent();
        placeId = intent.getStringExtra("placeId");
    System.out.println(placeId);
        imageBack = findViewById(R.id.imageBack);

        //setting the onClick listener for the back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourguideSingleView.super.onBackPressed();
            }
        });

        actionBar = findViewById(R.id.actionBar);
        actionBar.setText(R.string.attractionDetails);

        placeName = findViewById(R.id.placeName);
        placeAddress = findViewById(R.id.placeAddress);
        placeCity = findViewById(R.id.placeCity);
        placeDescription = findViewById(R.id.placeDescription);

        deletePlaceBtn = findViewById(R.id.deletePlaceBtn);
        editPlaceBtn = findViewById(R.id.editPlaceBtn);

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
                Toast.makeText(tourguideSingleView.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //edit places functionality
        editPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tourguideSingleView.this, editTouristAttraction.class);
                intent.putExtra("placeObj", attractionPlaces);
                startActivity(intent);
            }
        });


        //delete places functionality
        deletePlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(tourguideSingleView.this)
                        .setTitle("Warning")
                        .setMessage("This attraction place will be permanently deleted! Are you sure that you want to continue?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteData();
                            }
                        })
                        .show();
            }
        });
    }

    private void deleteData() {
        StorageReference imageRef;
        for (Map.Entry<String, String> entry : images.entrySet()) {
            imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(entry.getValue());
            imageRef.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(tourguideSingleView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        placesRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(tourguideSingleView.this, "Successfully Deleted the attraction place", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(tourguideSingleView.this, TourGuideMainView.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(tourguideSingleView.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}