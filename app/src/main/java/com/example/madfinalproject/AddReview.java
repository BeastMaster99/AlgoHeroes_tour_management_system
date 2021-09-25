package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddReview extends AppCompatActivity {
    RatingBar ratingBar;
    EditText review;
    Button submitBtn;
    float rateValue;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Review reviewObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_review);

        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.review);
        submitBtn = findViewById(R.id.submitBtn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Reviews").child("hotel_id").child("user_id_1");

        reviewObj = new Review();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateValue = ratingBar.getRating();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),  "rateValue: " + rateValue + " review: " + review.getText(), Toast.LENGTH_LONG).show();
                addDatatoFirebase(rateValue, review.getText().toString());
            }
        });
    }

    private void addDatatoFirebase(float rateValue, String review) {


        reviewObj.setReview(review);
        reviewObj.setRateValue(rateValue);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(reviewObj);
                Toast.makeText(AddReview.this, "Data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddReview.this, "Fail to add data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}



