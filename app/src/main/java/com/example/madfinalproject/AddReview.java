package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class AddReview extends AppCompatActivity {
    RatingBar ratingBar;
    EditText review;
    Button submitBtn;
    float rateValue;
    String hotelId;
    String travelerEmail;
    String fullName;

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

        ImageView goBackkBtn = (ImageView) findViewById(R.id.goBack);


        Intent intent = getIntent();
        hotelId = intent.getStringExtra("hotelId");

        String reviewId = UUID.randomUUID().toString();

        //getting the current traveler
        SessionsTraveler traveler = new SessionsTraveler(this);

        HashMap<String, String> travelerDetails = traveler.getTravelerDetailsFromSessions();
        travelerEmail = travelerDetails.get(SessionsTraveler.KEY_EMAIL);
        fullName = travelerDetails.get(SessionsTraveler.KEY_FIRSTNAME) + " " + travelerDetails.get(SessionsTraveler.KEY_LASTNAME);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Reviews").child(reviewId);

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
                Toast.makeText(getApplicationContext(),  "Thank You For The Feedback", Toast.LENGTH_SHORT).show();

                //validating the number of characters
                if(review.getText().toString().trim().length() < 1){
                    Toast.makeText(getApplicationContext(),  "Review should be more than 15 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(review.getText().toString().trim().length() > 500){
                    Toast.makeText(getApplicationContext(),  "Review should be less than 500 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                addDatatoFirebase(rateValue, review.getText().toString(), hotelId, travelerEmail, reviewId, fullName);
                Intent intent = new Intent(view.getContext(), HotelTravelerMainView.class);
                intent.putExtra("hotelId", reviewObj.getHotelId());
                finish();
                view.getContext().startActivity(intent);
            }
        });

        goBackkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addDatatoFirebase(float rateValue, String review, String hotelId, String traverId, String reviewId, String fullName) {
        reviewObj.setReview(review);
        reviewObj.setRateValue(rateValue);
        reviewObj.setHotelId(hotelId);;
        reviewObj.setTravelerId(traverId);
        reviewObj.setReviewId(reviewId);
        reviewObj.setFullName(fullName);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(reviewObj);
                Toast.makeText(AddReview.this, "Data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddReview.this, "Failed to add data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



