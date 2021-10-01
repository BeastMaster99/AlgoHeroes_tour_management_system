package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

public class AllPackages extends AppCompatActivity {

    private RecyclerView allPackagesRecView;

    RecyclerView recyclerView;
    ImageView imageBack,addPkgBtn;
    TextView actionBar;
    DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");
    String hotelId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_packages);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hotelId = extras.getString("hotelId");

        }
        System.out.println(hotelId);

        //action bar
        actionBar           = findViewById(R.id.actionBar);
        actionBar.setText("All Packages");

        imageBack           = findViewById(R.id.imageBack);

        addPkgBtn           = findViewById(R.id.addPkgBtn);
        allPackagesRecView  = findViewById(R.id.allPackagesRecView);


        //back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllPackages.super.onBackPressed();
            }
        });

        //Add button
        addPkgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllPackages.this,AddPackage.class);
                intent.putExtra("hotelId", hotelId);
                startActivity(intent);
            }
        });

        //hotel database Ref
        DatabaseReference packageRef = databaseReference.child("Packages");



        ArrayList<Package> packages = new ArrayList<>();

        //instantiating the adapter obj
        PackageRecViewAdapter adapter = new PackageRecViewAdapter(this);

        //setting packages in the adapter class
        adapter.setPackages(packages);
        allPackagesRecView.setAdapter(adapter);
        allPackagesRecView.setLayoutManager(new LinearLayoutManager(this));

        //getting the packages from the database
        packageRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Package package1 = dataSnapshot.getValue(Package.class);
                    packages.add(package1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllPackages.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}