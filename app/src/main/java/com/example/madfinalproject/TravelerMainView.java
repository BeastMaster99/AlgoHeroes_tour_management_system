package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class TravelerMainView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView title;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveler_main_view);

        drawerLayout = findViewById(R.id.drawlayout3);
        navigationView = findViewById(R.id.nav_menu_TR);
        title = findViewById(R.id.homeActionBarTitle);
        toolbar = findViewById(R.id.TR_home_action_bar);



        setSupportActionBar(toolbar);//adding the action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);//Disabling the default title

        title.setText("HI");//Passing the new title

        //Setting the header menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_TR);
        View headerView = navigationView.inflateHeaderView(R.layout.header_menu);
        ImageView backImage = (ImageView)headerView.findViewById(R.id.backImageMenuBar);
        TextView ownerName = (TextView)headerView.findViewById(R.id.userNameText);

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
        ownerName.setText(firstName + " " + lastName);

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
            case R.id.TRSignOut:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SessionsTraveler sessionsTraveler = new SessionsTraveler(TravelerMainView.this);
                        sessionsTraveler.travelerLogout();
                    }
                }, 250);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}