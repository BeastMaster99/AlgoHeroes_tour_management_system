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
import android.os.Bundle;
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

public class TravelerMainView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView title;
    Toolbar toolbar;
    RecyclerView recyclerView;

    ArrayList<Hotel> hotels = new ArrayList<>();
    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveler_main_view);

        drawerLayout = findViewById(R.id.drawlayout3);
        navigationView = findViewById(R.id.nav_menu_TR);
        title = findViewById(R.id.homeActionBarTitle);
        toolbar = findViewById(R.id.TR_home_action_bar);

        recyclerView = findViewById(R.id.hotelsRecView);

        setSupportActionBar(toolbar);//adding the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//Disabling the default title

        title.setText(R.string.traveler_hotel_title);//Passing the new title

        //hotel database Ref
        DatabaseReference hotelRef = databaseReference.child("Hotels");

        //Setting the header menu
        NavigationView navigationView = findViewById(R.id.nav_menu_TR);
        View headerView = navigationView.inflateHeaderView(R.layout.header_menu);
        ImageView backImage = headerView.findViewById(R.id.backImageMenuBar);
        TextView ownerName = headerView.findViewById(R.id.userNameText);

        //Creating the navigation drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(TravelerMainView.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //Navigation item select
        navigationView.setNavigationItemSelectedListener(this);

        //Setting the hotel owner name in navigation
        SessionsTraveler sessionsTraveler = new SessionsTraveler(TravelerMainView.this);
        HashMap<String,String> TravelerDetails = sessionsTraveler.getTravelerDetailsFromSessions();

        String firstName = TravelerDetails.get(SessionsHotelOwner.KEY_FIRSTNAME);
        String lastName = TravelerDetails.get(SessionsHotelOwner.KEY_LASTNAME);


        //Setting onclick listener for the back button
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        //Setting user name in the navigation
        ownerName.setText(firstName + ' ' + lastName);

        //instantiating the adapter obj
        HotelRecyclerAdapter adapter = new HotelRecyclerAdapter(this);
        //setting hotels in the adapter class
        adapter.setHotels(hotels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getting the hotels in the database
        hotelRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                    hotels.add(hotel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TravelerMainView.this, error.toString(), Toast.LENGTH_SHORT).show();
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
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.TRSignOut:
                SessionsTraveler sessionsTraveler = new SessionsTraveler(TravelerMainView.this);
                sessionsTraveler.travelerLogout();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}