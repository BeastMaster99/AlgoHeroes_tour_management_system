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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class HotelOwnerMainView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView title, hotelOwnerName;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_owner_main_view);

        drawerLayout = findViewById(R.id.drawlayout);
        navigationView = findViewById(R.id.nav_menu);
        title = findViewById(R.id.homeActionBarTitle);
        toolbar = findViewById(R.id.home_action_bar);
        hotelOwnerName = findViewById(R.id.text);

        setSupportActionBar(toolbar);//adding the action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);//Disabling the default title

        title.setText("Your Hotels");//Passing the new title

        //Setting the header menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu);
        View headerView = navigationView.inflateHeaderView(R.layout.header_menu);
        ImageView backImage = (ImageView)headerView.findViewById(R.id.backImageMenuBar);
        TextView ownerName = (TextView)headerView.findViewById(R.id.userNameText);

        //creating session hotel owner object and validating the login
        SessionsHotelOwner sessionsHotelOwner = new SessionsHotelOwner(HotelOwnerMainView.this);
        if (sessionsHotelOwner.checkHotelOwnerLogin() == false){
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish();
        }

        //Creating the navigation drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(HotelOwnerMainView.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //Navigation item select
        navigationView.setNavigationItemSelectedListener(this);

        //Setting the hotel owner name in navigation
        SessionsHotelOwner sessionsManagerHotelOwner = new SessionsHotelOwner(HotelOwnerMainView.this);
        HashMap <String,String> hotelOwnerDetails = sessionsManagerHotelOwner.getHotelOwnerDetailsFromSessions();

        String firstName = hotelOwnerDetails.get(SessionsHotelOwner.KEY_FIRSTNAME);
        String lastName = hotelOwnerDetails.get(SessionsHotelOwner.KEY_LASTNAME);

        hotelOwnerName.setText(firstName + " " + lastName);


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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.HOSignOut:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SessionsHotelOwner sessions = new SessionsHotelOwner(HotelOwnerMainView.this);
                        sessions.hotelOwnerLogout();
                    }
                }, 250);
                break;
            case R.id.HOAddHotel:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HotelOwnerMainView.this, AddHotel.class);
                        startActivity(intent);
                    }
                }, 250);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}