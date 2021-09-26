package com.example.madfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class AllPackages extends AppCompatActivity {

    private RecyclerView allPackagesRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_packages);

        //allPackagesRecView = findViewById(R.id.allPackagesRecView);

//        ArrayList<Package> packages = new ArrayList<>();
//        packages.add(new Package("Hertage Kandalama", "3", "1200.00", "hvghjvlhlv","1"));

//        PackageRecViewAdapter adapter = new PackageRecViewAdapter(this);
//        adapter.setPackages(packages);
//
//        allPackagesRecView.setAdapter(adapter);
//        allPackagesRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}