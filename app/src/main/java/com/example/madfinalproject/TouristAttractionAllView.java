package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.os.Bundle;
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
    Toolbar toolbar;
    RecyclerView recyclerView;

    ArrayList<AttractionPlaces> attractionPlaces = new ArrayList<>();
    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_attraction_all_view);

        title = findViewById(R.id.homeActionBarTitle);
        toolbar = findViewById(R.id.TG_home_action_bar);

        recyclerView = findViewById(R.id.placeRecView);

        setSupportActionBar(toolbar);//adding the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//Disabling the default title

        title.setText("Tourist Attractions");//Passing the new title

        //place database Ref
        DatabaseReference placesRef = databaseReference.child("Places");

        //instantiating the adapter obj
        TouristAttractionsRecyclerAdapter adapter = new TouristAttractionsRecyclerAdapter(this);
        //setting places in the adapter class
        adapter.setAttractionPlaces(attractionPlaces);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

}