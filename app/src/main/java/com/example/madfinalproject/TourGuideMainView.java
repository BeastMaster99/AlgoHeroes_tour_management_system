package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TourGuideMainView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView title;
    Toolbar toolbar;
    RecyclerView recyclerView;

    ArrayList<AttractionPlaces> attractionPlaces = new ArrayList<>();
    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide_main_view);

        drawerLayout = findViewById(R.id.drawlayout2);
        navigationView = findViewById(R.id.nav_menu_TG);
        title = findViewById(R.id.homeActionBarTitle);
        toolbar = findViewById(R.id.TG_home_action_bar);

        recyclerView = findViewById(R.id.placesRecView);

        setSupportActionBar(toolbar);//adding the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//Disabling the default title

        title.setText("Your Tourist Attractions");//Passing the new title

        //place database Ref
        DatabaseReference placesRef = databaseReference.child("Places");

        //creating session tour guide object and validating the login
        SessionsTourGuide sessionsTourGuide1 = new SessionsTourGuide(TourGuideMainView.this);
        if (sessionsTourGuide1.checkTourGuideLogin() == false) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish();
        }

        //Setting the header menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_TG);
        View headerView = navigationView.inflateHeaderView(R.layout.header_menu);
        ImageView backImage = (ImageView)headerView.findViewById(R.id.backImageMenuBar);
        TextView ownerName = (TextView)headerView.findViewById(R.id.userNameText);


        //Creating the navigation drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(TourGuideMainView.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //Navigation item select
        navigationView.setNavigationItemSelectedListener(this);

        //Setting the tour guide name in navigation
        SessionsTourGuide sessionsTourGuide = new SessionsTourGuide(TourGuideMainView.this);
        HashMap<String,String> TourGuideDetails = sessionsTourGuide.getTourGuideDetailsFromSessions();

        String firstName = TourGuideDetails.get(SessionsHotelOwner.KEY_FIRSTNAME);
        String lastName = TourGuideDetails.get(SessionsHotelOwner.KEY_LASTNAME);
        String tourguideEmail = TourGuideDetails.get(SessionsTourGuide.KEY_EMAIL);

        //Setting onclick listener for the back button
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        //Setting user name in the navigation
        ownerName.setText(firstName + " " + lastName);

        //instantiating the adapter obj
        placeRecyclerAdapter adapter = new placeRecyclerAdapter(this);
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
                    if(place.getTourGuide().equalsIgnoreCase(tourguideEmail)){
                        attractionPlaces.add(place);

                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TourGuideMainView.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    //Avoiding closing the the current activity when user press back button
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //To Select navigation Items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.TGSignOut:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SessionsTourGuide sessionsTourGuide = new SessionsTourGuide(TourGuideMainView.this);
                        sessionsTourGuide.tourGuideLogout();
                    }
                }, 250);
                break;
            case R.id.TGAddAttractions:
                Intent intent = new Intent(TourGuideMainView.this, addTouristAttraction.class);
                startActivity(intent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}