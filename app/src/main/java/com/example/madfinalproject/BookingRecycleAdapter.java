package com.example.madfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingRecycleAdapter extends RecyclerView.Adapter<BookingRecycleAdapter.ViewHolder> {

    Context context;

    ArrayList <HotelBookings> list;

    public BookingRecycleAdapter(Context context, ArrayList<HotelBookings> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.traveler_all_bookings_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HotelBookings hotelBookings = list.get(position);
        holder.travelerHotelName.setText(hotelBookings.getHotelName());
        holder.travelerDuration.setText(hotelBookings.getCheckInDate() + " To " + hotelBookings.getCheckOutDate());
        holder.travelerNumberOfRooms.setText(hotelBookings.getNumberOfRooms() + " Room(s)");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView travelerHotelName, travelerRoomType, travelerDuration, travelerNumberOfRooms;
        Button travelerBookingsEdit, travelerBookingsCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            travelerHotelName = itemView.findViewById(R.id.travelerHotelName);
            travelerRoomType = itemView.findViewById(R.id.travelerRoomType);
            travelerDuration = itemView.findViewById(R.id.travelerDuration);
            travelerNumberOfRooms = itemView.findViewById(R.id.travelerNumberOfRooms);
            travelerBookingsEdit = itemView.findViewById(R.id.travelerBookingsEdit);
            travelerBookingsCancel =itemView.findViewById(R.id.travelerBookingsCancel);
        }
    }
}
