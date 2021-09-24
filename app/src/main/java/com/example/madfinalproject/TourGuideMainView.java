package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class TourGuideMainView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView title;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide_main_view);

        drawerLayout = findViewById(R.id.drawlayout2);
        navigationView = findViewById(R.id.nav_menu_TG);
        title = findViewById(R.id.homeActionBarTitle);
        toolbar = findViewById(R.id.TG_home_action_bar);

        setSupportActionBar(toolbar);//adding the action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);//Disabling the default title

        title.setText("Attractions");//Passing the new title

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

        //Setting the hotel owner name in navigation
        SessionsTourGuide sessionsTourGuide = new SessionsTourGuide(TourGuideMainView.this);
        HashMap<String,String> TourGuideDetails = sessionsTourGuide.getTourGuideDetailsFromSessions();

        String firstName = TourGuideDetails.get(SessionsHotelOwner.KEY_FIRSTNAME);
        String lastName = TourGuideDetails.get(SessionsHotelOwner.KEY_LASTNAME);


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
            case R.id.TGSignOut:
                SessionsTourGuide sessionsTourGuide = new SessionsTourGuide(TourGuideMainView.this);
                sessionsTourGuide.tourGuideLogout();
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