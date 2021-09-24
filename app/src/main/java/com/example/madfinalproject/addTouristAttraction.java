package com.example.madfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class addTouristAttraction extends AppCompatActivity {

    DatabaseReference databaseReference;
//    StorageReference storageReference;

    ArrayList<Uri> imageURIs = new ArrayList<>();

    TextView ;
    EditText ;
    ImageView imageView6;
    Button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide(); //Hide the action bar
        setContentView(R.layout.activity_add_tourist_attraction);

        imageView6 = findViewById(R.id.imageView6);

        //setting the onClick listener for the back button
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTouristAttraction.super.onBackPressed();
            }
        });
    }
}