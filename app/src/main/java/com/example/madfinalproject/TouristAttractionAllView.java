package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
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
import java.util.Objects;

public class TouristAttractionAllView extends AppCompatActivity {

    TextView title;
    RecyclerView recyclerView;
    ImageView backImage;

    ArrayList<AttractionPlaces> attractionPlaces = new ArrayList<>();
    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_attraction_all_view);

        title = findViewById(R.id.actionBar);

        recyclerView = findViewById(R.id.placeRecView);

        title.setText("Tourist Attractions");//Passing the new title

        backImage = findViewById(R.id.imageBack);

        //place database Ref
        DatabaseReference placesRef = databaseReference.child("Places");

        //instantiating the adapter obj
        TouristAttractionsRecyclerAdapter adapter = new TouristAttractionsRecyclerAdapter(this);
        //setting places in the adapter class
        adapter.setAttractionPlaces(attractionPlaces);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setting the onClick listener for the back button
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TouristAttractionAllView.super.onBackPressed();
            }
        });

        //getting the places in the database
        placesRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AttractionPlaces place = dataSnapshot.getValue(AttractionPlaces.class);
                    attractionPlaces.add(place);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TouristAttractionAllView.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //to refresh on restart
        finish();
        startActivity(getIntent()); //starting same activity by using the same intent
    }


}