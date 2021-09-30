package com.example.madfinalproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ViewHolder> {
    private ArrayList<Review> reviews = new ArrayList<>();
    private final Context context;
    private String travelerEmail;

    public ReviewsRecyclerAdapter(Context context, String travelerEmail) {
        this.context = context;
        this.travelerEmail = travelerEmail;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int itemPosition = holder.getAdapterPosition();
        holder.travelerId.setText(reviews.get(position).getFullName());
        holder.review.setText(reviews.get(position).getReview());
        holder.rateValue.setRating(reviews.get(position).getRateValue());

        holder.setIsRecyclable(false);


        if(reviews.get(position).getTravelerId().equals(travelerEmail)){
            holder.editReviewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditReview.class);
                    intent.putExtra("reviewId", reviews.get(itemPosition).getReviewId());
                    intent.putExtra("rateValue", reviews.get(itemPosition).getRateValue());
                    intent.putExtra("review", reviews.get(itemPosition).getReview());
                    intent.putExtra("hotelId", reviews.get(itemPosition).getHotelId());
                    intent.putExtra("fullName", reviews.get(itemPosition).getFullName());
                    intent.putExtra("travelerId", reviews.get(itemPosition).getTravelerId());
                    context.startActivity(intent);
                }
            });

            holder.deleteReviewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase firebaseDatabase;
                    DatabaseReference databaseReference;

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference("Reviews").child(reviews.get(itemPosition).getReviewId()).removeValue();

                    Intent intent = new Intent(context, HotelTravelerMainView.class);
                    intent.putExtra("hotelId", reviews.get(itemPosition).getHotelId());
                    context.startActivity(intent);
                }
            });
        } else {
            holder.editReviewBtn.setVisibility(View.GONE);
            holder.deleteReviewBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView travelerId, review;
        private final RatingBar rateValue;
        private final Button editReviewBtn, deleteReviewBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.travelerId = itemView.findViewById(R.id.travelerName);
            this.review = itemView.findViewById(R.id.travelerReview);
            this.rateValue = itemView.findViewById(R.id.travelerRating);
            this.editReviewBtn = itemView.findViewById(R.id.editReviewBtn);
            this.deleteReviewBtn =  itemView.findViewById(R.id.deleteReviewBtn);
        }
    }
}
