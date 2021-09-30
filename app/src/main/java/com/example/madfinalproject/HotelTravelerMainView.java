package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HotelTravelerMainView extends AppCompatActivity {
    String hotelId;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    TextView actionBar, hotelName, HotelRating, hotelAmenities, hotelAddress, hotelContact, hotelCity, hotelDescription;
    ImageView imageBack, favBtnWhite, favBtnBlack;
    SliderView sliderView;

    Button reviewBtn;

    RecyclerView reviewRecycleView;
    ArrayList<Review> reviews = new ArrayList<>();

    Button reserveBtn;

    Hotel hotel = new Hotel();
    FavouriteHotels favData = new FavouriteHotels();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_traveler_main_view);

        Intent intent = getIntent();
        hotelId = intent.getStringExtra("hotelId");

        imageBack = findViewById(R.id.imageBack);
        favBtnWhite = findViewById(R.id.favBtnWhite);
        favBtnBlack = findViewById(R.id.favBtnBlack);

        SessionsTraveler traveler = new SessionsTraveler(this);

        HashMap<String, String> travelerDetails = traveler.getTravelerDetailsFromSessions();
        String travelerEmail = travelerDetails.get(SessionsTraveler.KEY_EMAIL);

        //setting the onClick listener for the back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelTravelerMainView.super.onBackPressed();
            }
        });

        actionBar = findViewById(R.id.actionBar);
        actionBar.setText(R.string.hotel_detail_title);

        hotelName = findViewById(R.id.hotelName);
        HotelRating = findViewById(R.id.HotelRating);
        hotelAmenities = findViewById(R.id.hotelAmenities);
        hotelAddress = findViewById(R.id.hotelAddress);
        hotelContact = findViewById(R.id.hotelContact);
        hotelCity = findViewById(R.id.hotelCity);
        hotelDescription = findViewById(R.id.hotelDescription);


        reviewRecycleView = findViewById(R.id.travelerReviews);

        reviewBtn = findViewById(R.id.reviewBtn);


        reserveBtn = findViewById(R.id.reserveBtn);


        sliderView = findViewById(R.id.imageSlider);
        //instantiating the slider adapter
        HotelImageSliderAdapter adapter = new HotelImageSliderAdapter(this);


        //setting the slider view
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        //hotel database Ref
        DatabaseReference hotelRef = databaseReference.child("Hotels").child(hotelId);
        //fav database Ref
        DatabaseReference favRef = databaseReference.child("Favourite Hotels");

        //checking fav status
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FavouriteHotels fav = dataSnapshot.getValue(FavouriteHotels.class);
                    assert fav != null;
                    if (fav.getHotelId().equalsIgnoreCase(hotelId) && fav.getUserEmail().equalsIgnoreCase(travelerEmail)) {
                        favData = fav; //to handle fav related stuff such as deletions
                        favBtnWhite.setVisibility(View.GONE);
                        favBtnBlack.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        favBtnWhite.setVisibility(View.VISIBLE);
                        favBtnBlack.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HotelTravelerMainView.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        //adding to fav logic
        favBtnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String favId = UUID.randomUUID().toString();
                FavouriteHotels newFavEntry = new FavouriteHotels(favId, travelerEmail, hotelId);
                favRef.child(favId).setValue(newFavEntry).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HotelTravelerMainView.this, "Failed! Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(HotelTravelerMainView.this, hotel.getName() + " Added to Your Favourites", Toast.LENGTH_SHORT).show();
                        favBtnWhite.setVisibility(View.GONE);
                        favBtnBlack.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        favBtnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favRef.child(favData.getFavId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(HotelTravelerMainView.this, "Removed " + hotel.getName() + " From the Favourites", Toast.LENGTH_SHORT).show();
                        favBtnWhite.setVisibility(View.VISIBLE);
                        favBtnBlack.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HotelTravelerMainView.this, "Failed! Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //getting the hotels in the database
        hotelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    hotel = snapshot.getValue(Hotel.class);

                    assert hotel != null;
                    adapter.setInputUrls(hotel.getImages());
                    adapter.notifyDataSetChanged();

                    hotelName.setText(hotel.getName());
                    HotelRating.setText("9.6");
                    hotelAddress.setText(hotel.getAddress());
                    hotelContact.setText(hotel.getContact());
                    hotelCity.setText(hotel.getCity());
                    hotelDescription.setText(hotel.getDescription());

                    StringBuilder amenities = new StringBuilder();

                    for (int i = 0; i < hotel.getAmenities().size(); i++) {
                        amenities.append(hotel.getAmenities().get(i));
                        if (i != hotel.getAmenities().size() - 1) {
                            amenities.append(", ");
                        }
                    }

                    hotelAmenities.setText(amenities.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HotelTravelerMainView.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        Query reviewsQuery = databaseReference.child("Reviews").orderByChild("hotelId").equalTo(hotelId);

        ReviewsRecyclerAdapter reviewsAdapter = new ReviewsRecyclerAdapter(this, travelerEmail);

        reviewsAdapter.setReviews(reviews);
        reviewRecycleView.setAdapter(reviewsAdapter);
        reviewRecycleView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        reviewsQuery.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                reviews.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Review review = dataSnapshot.getValue(Review.class);
                    reviews.add(review);
                }
                reviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HotelTravelerMainView.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HotelTravelerMainView.this, AddReview.class);
                intent1.putExtra("hotelId", hotelId);
                startActivity(intent1);

            }
        });

        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HotelTravelerMainView.this, BookHotel.class);
                intent1.putExtra("HotelId", hotelId);
                intent1.putExtra("hotelName", hotel.getName());
                intent1.putExtra("hotelOwnerEmail", hotel.getOwner());

                startActivity(intent1);
            }
        });
    }
}
