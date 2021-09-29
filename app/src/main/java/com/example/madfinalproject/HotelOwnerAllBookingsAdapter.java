package com.example.madfinalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HotelOwnerAllBookingsAdapter extends RecyclerView.Adapter<HotelOwnerAllBookingsAdapter.ViewHolder>{

    Context context;
    ArrayList <HotelBookings> list = new ArrayList<>();

    public HotelOwnerAllBookingsAdapter(Context context, ArrayList<HotelBookings> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_owner_all_bookings_item, parent, false);
        return new HotelOwnerAllBookingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int itemPosition = holder.getAdapterPosition();
        holder.HOAllBookingsHotelName.setText(list.get(itemPosition).getHotelName());
        holder.HOAllBookingsRooms.setText(list.get(itemPosition).getNumberOfRooms() + " Room(s)");
        holder.HOAllBookingsDates.setText((list.get(itemPosition).getCheckInDate()) + " To " + list.get(itemPosition).getCheckOutDate());
        holder.HOAllBookingsTravelerName.setText("By " + list.get(itemPosition).getTravelerFirstName());



        //To redirect to the dial pad
        holder.HOAllBookingsContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + list.get(itemPosition).getTravelerContactNumber()));
                context.startActivity(intent);
            }
        });

        //To redirect user to the email
        holder.HOAllBookingsEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);

                String email = list.get(itemPosition).getTravelerEmail();
                email = email.replace(",", ".");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.setType("message/rfc822");
                context.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView HOAllBookingsHotelName, HOAllBookingsRooms, HOAllBookingsDates, HOAllBookingsTravelerName;
        Button HOAllBookingsContact, HOAllBookingsEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            HOAllBookingsHotelName = itemView.findViewById(R.id.HOAllBookingsHotelName);
            HOAllBookingsRooms = itemView.findViewById(R.id.HOAllBookingsRooms);
            HOAllBookingsDates = itemView.findViewById(R.id.HOAllBookingsDates);
            HOAllBookingsTravelerName = itemView.findViewById(R.id.HOAllBookingsTravelerName);
            HOAllBookingsContact = itemView.findViewById(R.id.HOAllBookingsContact);
            HOAllBookingsEmail = itemView.findViewById(R.id.HOAllBookingsEmail);
        }
    }
}
