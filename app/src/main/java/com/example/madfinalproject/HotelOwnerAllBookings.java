package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;

public class HotelOwnerAllBookings extends AppCompatActivity {

    TextView title;
    ImageView backImage;
    RecyclerView recyclerView;
    ArrayList<HotelBookings> list;
    HotelOwnerAllBookingsAdapter hotelOwnerAllBookingsAdapter;
    String hotelOwnerEmail;

    //Creating object to access firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_owner_bookings);

        backImage = findViewById(R.id.imageBack);
        title = findViewById(R.id.actionBar);

        //Setting page title
        title.setText("Bookings");

        //Setup a onclick listener for the back image
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelOwnerAllBookings.super.onBackPressed();
            }
        });

        //getting the current hotel owner
        SessionsHotelOwner hotelOwner = new SessionsHotelOwner(this);

        HashMap<String, String> hotelOwnerDetails = hotelOwner.getHotelOwnerDetailsFromSessions();
        hotelOwnerEmail = hotelOwnerDetails.get(SessionsTraveler.KEY_EMAIL);


        recyclerView = findViewById(R.id.hotelOwnerAllBookingsRecView);
        list = new ArrayList<>();

        hotelOwnerAllBookingsAdapter = new HotelOwnerAllBookingsAdapter(this, list);
        recyclerView.setAdapter(hotelOwnerAllBookingsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getting teh data from the database
        databaseReference.child("Hotel Bookings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HotelBookings hotelBookings = dataSnapshot.getValue(HotelBookings.class);
                    if(hotelBookings.getHotelOwnerEmail().equalsIgnoreCase(hotelOwnerEmail)){
                        list.add(hotelBookings);
                    }
                }
                hotelOwnerAllBookingsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HotelOwnerAllBookings.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}