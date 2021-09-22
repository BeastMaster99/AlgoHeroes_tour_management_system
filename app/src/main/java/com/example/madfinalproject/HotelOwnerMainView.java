package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
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
                SessionsHotelOwner sessions = new SessionsHotelOwner(HotelOwnerMainView.this);
                sessions.hotelOwnerLogout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}