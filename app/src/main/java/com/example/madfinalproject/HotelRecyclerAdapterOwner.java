package com.example.madfinalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

public class HotelRecyclerAdapterOwner extends RecyclerView.Adapter<HotelRecyclerAdapterOwner.ViewHolder>{
    private ArrayList<Hotel> hotels = new ArrayList<>();
    private final Context context;

    public HotelRecyclerAdapterOwner(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int itemPosition = holder.getAdapterPosition();
        holder.hotelName.setText(hotels.get(position).getName());
        holder.hotelCity.setText(hotels.get(position).getCity());
        String displayImgUrl = "";

        for ( Map.Entry<String, String> entry : hotels.get(position).getImages().entrySet()) {
            displayImgUrl = entry.getValue();
            break;
        }

        Glide.with(context)
                .asBitmap()
                .load(displayImgUrl)
                .into(holder.hotelImgId);

        holder.visitHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HotelHotelOwnerMainView.class);
                intent.putExtra("hotelId", hotels.get(itemPosition).getHotelId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public void setHotels(ArrayList<Hotel> hotels) {
        this.hotels = hotels;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView hotelImgId;
        private final TextView hotelName, hotelCity;
        private final Button visitHotelBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImgId = itemView.findViewById(R.id.hotelImgId);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelCity = itemView.findViewById(R.id.hotelCity);
            visitHotelBtn = itemView.findViewById(R.id.visitHotelBtn);
        }
    }
}
