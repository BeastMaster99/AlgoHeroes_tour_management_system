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

public class HotelOwnerReviewRecycler extends RecyclerView.Adapter<HotelOwnerReviewRecycler.ViewHolder> {
    private ArrayList<Review> reviews = new ArrayList<>();
    private final Context context;

    public HotelOwnerReviewRecycler(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ownerreviewlist, parent, false);
        return new HotelOwnerReviewRecycler.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int itemPosition = holder.getAdapterPosition();
        holder.travelerId.setText(reviews.get(position).getFullName());
        holder.review.setText(reviews.get(position).getReview());
        holder.rateValue.setRating(reviews.get(position).getRateValue());

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() { return reviews.size(); }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView travelerId, review;
        private final RatingBar rateValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.travelerId = itemView.findViewById(R.id.travelerName);
            this.review = itemView.findViewById(R.id.travelerReview);
            this.rateValue = itemView.findViewById(R.id.travelerRating);
        }
    }
}















