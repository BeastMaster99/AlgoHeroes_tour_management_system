package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
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

public class EditReview extends AppCompatActivity {
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

        setContentView(R.layout.activity_edit_review);

        reviewObj = new Review();

        Bundle bundle = getIntent().getExtras();

        reviewObj.setReviewId(bundle.getString("reviewId"));
        reviewObj.setRateValue(bundle.getFloat("rateValue"));
        reviewObj.setReview(bundle.getString("review"));
        reviewObj.setHotelId(bundle.getString("hotelId"));
        reviewObj.setFullName(bundle.getString("fullName"));
        reviewObj.setTravelerId(bundle.getString("travelerId"));

        ratingBar = findViewById(R.id.ratingBar);
        submitBtn = findViewById(R.id.editReviewBtn);
        review = findViewById(R.id.review);


        ratingBar.setRating(reviewObj.getRateValue());
        review.setText(reviewObj.getReview());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Reviews").child(reviewObj.getReviewId());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateValue = ratingBar.getRating();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Thank You For The Feedback", Toast.LENGTH_LONG).show();
//
                reviewObj.setReview(review.getText().toString());
                reviewObj.setRateValue(rateValue);
                addDatatoFirebase(reviewObj);

                Intent intent = new Intent(view.getContext(), HotelTravelerMainView.class);
                intent.putExtra("hotelId", reviewObj.getHotelId());
                view.getContext().startActivity(intent);
                finish();
            }
        });


    }

    private void addDatatoFirebase(Review review) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(reviewObj);
                Toast.makeText(EditReview.this, "Data edited", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditReview.this, "Fail to edit data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}