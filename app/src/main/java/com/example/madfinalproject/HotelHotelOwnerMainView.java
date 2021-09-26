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

public class HotelHotelOwnerMainView extends AppCompatActivity {
    String hotelId;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    TextView actionBar, hotelName, HotelRating, hotelAmenities, hotelAddress, hotelContact, hotelCity, hotelDescription;
    ImageView imageBack;
    SliderView sliderView;
    Button deleteHotelBtn;

    Hotel hotel = new Hotel();

    HashMap<String, String> images = new HashMap<>();
    
    DatabaseReference hotelRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_hotel_owner_main_view);

        Intent intent = getIntent();
        hotelId = intent.getStringExtra("hotelId");

        imageBack = findViewById(R.id.imageBack);

        //setting the onClick listener for the back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelHotelOwnerMainView.super.onBackPressed();
            }
        });

        actionBar = findViewById(R.id.actionBar);
        actionBar.setText(R.string.hotel_detail_title);

        hotelName = findViewById(R.id.hotelName);
        HotelRating = findViewById(R.id.HotelRating);
        hotelAmenities = findViewById(R.id.hotelAmenities);
        hotelAddress = findViewById(R.id.hotelAddress);
        hotelContact = findViewById(R.id.hotelContact);
        hotelCity = findViewById(R.id.hotelCity);
        hotelDescription = findViewById(R.id.hotelDescription);

        deleteHotelBtn = findViewById(R.id.deleteHotelBtn);

        sliderView = findViewById(R.id.imageSlider);
        //instantiating the slider adapter
        HotelImageSliderAdapter adapter = new HotelImageSliderAdapter(this);


        //setting the slider view
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        //hotel database Ref
         hotelRef = databaseReference.child("Hotels").child(hotelId);

        //getting the hotels in the database
        hotelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    hotel = snapshot.getValue(Hotel.class);

                    assert hotel != null;
                    adapter.setInputUrls(hotel.getImages());
                    adapter.notifyDataSetChanged();

                    hotelName.setText(hotel.getName());
                    HotelRating.setText("9.6");
                    hotelAddress.setText(hotel.getAddress());
                    hotelContact.setText(hotel.getContact());
                    hotelCity.setText(hotel.getCity());
                    hotelDescription.setText(hotel.getDescription());

                    StringBuilder amenities = new StringBuilder();

                    for (int i = 0; i < hotel.getAmenities().size(); i++) {
                        amenities.append(hotel.getAmenities().get(i));
                        if (i != hotel.getAmenities().size() - 1) {
                            amenities.append(", ");
                        }
                    }

                    hotelAmenities.setText(amenities.toString());
                    images = hotel.getImages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HotelHotelOwnerMainView.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //delete hotels functionality
        deleteHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HotelHotelOwnerMainView.this)
                        .setTitle("Warning")
                        .setMessage("This hotel will be permanently deleted! Are you sure that you want to continue?")
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

    private void deleteData(){
        StorageReference imageRef;
        for (Map.Entry<String,String> entry : images.entrySet()){
            imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(entry.getValue());
            imageRef.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HotelHotelOwnerMainView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        hotelRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(HotelHotelOwnerMainView.this, "Successfully Deleted the Hotel", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HotelHotelOwnerMainView.this, HotelOwnerMainView.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HotelHotelOwnerMainView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}