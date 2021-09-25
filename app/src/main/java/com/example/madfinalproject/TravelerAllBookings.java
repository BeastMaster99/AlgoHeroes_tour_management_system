package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;

public class TravelerAllBookings extends AppCompatActivity {

    TextView title;
    ImageView backImage;
    RecyclerView recyclerView;
    BookingRecycleAdapter bookingRecycleAdapter;
    ArrayList <HotelBookings> list;
    String travelerEmail;

    //Creating object to access firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveler_all_bookings);

        backImage = findViewById(R.id.imageBack);
        title = findViewById(R.id.actionBar);


        //creating session traveler object and validating the login
        SessionsTraveler sessionsTraveler = new SessionsTraveler(TravelerAllBookings.this);
        if (sessionsTraveler.checkTravelerGuideLogin() == false){
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish();
        }


        //Setting page title
        title.setText("Your Bookings");

        //Setup a onclick listener for the back image
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TravelerAllBookings.super.onBackPressed();
            }
        });

        //getting the current traveler
        SessionsTraveler traveler = new SessionsTraveler(this);

        HashMap<String, String> travelerDetails = traveler.getTravelerDetailsFromSessions();
        travelerEmail = travelerDetails.get(SessionsTraveler.KEY_EMAIL);


        recyclerView = findViewById(R.id.travelerAllBookingsRecView);

        list = new ArrayList<>();
        bookingRecycleAdapter = new BookingRecycleAdapter(this, list);
        recyclerView.setAdapter(bookingRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getting teh data from the database
        databaseReference.child("Hotel Bookings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        HotelBookings hotelBookings = dataSnapshot.getValue(HotelBookings.class);
                        if(hotelBookings.getTravelerEmail().equalsIgnoreCase(travelerEmail)){
                            list.add(hotelBookings);
                        }
                    }
                    bookingRecycleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TravelerAllBookings.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}