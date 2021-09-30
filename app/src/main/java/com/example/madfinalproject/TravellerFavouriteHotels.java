package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
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

public class TravellerFavouriteHotels extends AppCompatActivity {

    ArrayList<Hotel> hotels = new ArrayList<>();
    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    RecyclerView recyclerView;
    ImageView imageBack;
    TextView actionBar;

    //instantiating the adapter obj
    HotelRecyclerAdapter adapter = new HotelRecyclerAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travller_favourite_hotels);

        actionBar = findViewById(R.id.actionBar);
        imageBack = findViewById(R.id.imageBack);
        recyclerView = findViewById(R.id.hotelsRecView);

        //setting the activity title
        actionBar.setText(R.string.fav_title);

        SessionsTraveler traveler = new SessionsTraveler(this);
        HashMap<String, String> travelerDetails = traveler.getTravelerDetailsFromSessions();
        String travelerEmail = travelerDetails.get(SessionsTraveler.KEY_EMAIL);

        //setting the onClick listener for the back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TravellerFavouriteHotels.super.onBackPressed();
            }
        });


        //setting hotels in the adapter class
        adapter.setHotels(hotels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //hotel database Ref
        DatabaseReference hotelRef = databaseReference.child("Hotels");

        //favourites database Ref
        DatabaseReference favRef = databaseReference.child("Favourite Hotels");

        //loading fav hotels
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FavouriteHotels fav = dataSnapshot.getValue(FavouriteHotels.class);
                    assert fav != null;
                    if (fav.getUserEmail().equalsIgnoreCase(travelerEmail)){
                        //getting the hotels in the database
                        hotelRef.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                                    assert hotel != null;
                                    if(hotel.getHotelId().equalsIgnoreCase(fav.getHotelId())){
                                        hotels.add(hotel);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(TravellerFavouriteHotels.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TravellerFavouriteHotels.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //to handle in case returned back after removing the hotel from favourites
        finish();
        startActivity(getIntent()); //starting same activity by using the same intent
    }
}