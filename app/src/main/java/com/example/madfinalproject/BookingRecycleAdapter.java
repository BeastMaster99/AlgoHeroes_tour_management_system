package com.example.madfinalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingRecycleAdapter extends RecyclerView.Adapter<BookingRecycleAdapter.ViewHolder> {

    Context context;
    BookingRecycleAdapter bookingRecycleAdapter;
    HotelBookings hotelBookings;


    ArrayList<HotelBookings> list;

    //Creating object to access firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    public BookingRecycleAdapter(Context context, ArrayList<HotelBookings> list) {
        this.context = context;
        this.list = list;
        this.bookingRecycleAdapter = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.traveler_all_bookings_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int itemPosition = holder.getAdapterPosition();
        hotelBookings = list.get(position);
        holder.travelerHotelName.setText(hotelBookings.getHotelName());
        holder.travelerDuration.setText(hotelBookings.getCheckInDate() + " To " + hotelBookings.getCheckOutDate());
        holder.travelerNumberOfRooms.setText(hotelBookings.getNumberOfRooms() + " Room(s)");

        //setup a onclickListener for delete the data form the firebase
        holder.travelerBookingsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Warning!!!")
                        .setMessage("This reservation will be canceled! Are you sure that you want to continue?")
                        .setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cancelReservation(itemPosition);

                            }
                        })
                        .show();

            }
        });
    }

    //For cancel reservation
    private void cancelReservation(int itemPosition) {
        databaseReference.child("Hotel Bookings").child(hotelBookings.getUuid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    list.clear();
//                    list.remove(itemPosition);
//                    notifyItemRemoved(itemPosition);
//                    notifyItemRangeChanged(itemPosition, list.size());
                    //notifyDataSetChanged();
                    Intent intent =  new Intent(context, TravelerAllBookings.class);
                    context.startActivity(intent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView travelerHotelName, travelerRoomType, travelerDuration, travelerNumberOfRooms;
        Button travelerBookingsEdit, travelerBookingsCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            travelerHotelName = itemView.findViewById(R.id.travelerHotelName);
            travelerRoomType = itemView.findViewById(R.id.travelerRoomType);
            travelerDuration = itemView.findViewById(R.id.travelerDuration);
            travelerNumberOfRooms = itemView.findViewById(R.id.travelerNumberOfRooms);
            travelerBookingsEdit = itemView.findViewById(R.id.travelerBookingsEdit);
            travelerBookingsCancel = itemView.findViewById(R.id.travelerBookingsCancel);
        }
    }
}
