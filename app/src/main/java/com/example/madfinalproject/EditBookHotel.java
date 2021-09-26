package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditBookHotel extends AppCompatActivity {

    Spinner numberOfRoomsEdit;
    ImageView backImage;
    TextView title;
    EditText extraDetailsEdit;
    Button checkInDateEdit, checkOutDateEdit, bookHotelSubmitButtonEdit;
    int year, month, day;
    AlertDialog.Builder builder;
    String checkInDateTextEdit, chekOutDateTextEdit, numberOfRoomsTextEdit, extraDetailsTextEdit, travelerEmailEdit, travelerFirstNameEdit, travelerContactNumberEdit,
            hotelNameEdit, hotelIdEdit, uuidEdit, hotelOwnerEmailEdit;


    //Creating object to access firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_hotel);

        backImage = findViewById(R.id.imageBack);
        title = findViewById(R.id.actionBar);
        checkInDateEdit = findViewById(R.id.checkInDateEdit);
        checkOutDateEdit = findViewById(R.id.checkOutDateEdit);
        numberOfRoomsEdit = findViewById(R.id.numberOfRoomsEdit);
        extraDetailsEdit = findViewById(R.id.bookHotelExtraDetailsEdit);
        bookHotelSubmitButtonEdit = findViewById(R.id.bookHotelSubmitButtonEdit);

        //Setting page title
        title.setText("Edit Booked Hotel");

        //Setup a onclick listener for the back image
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditBookHotel.super.onBackPressed();
            }
        });

        //Getting data from the allBookings
        Intent intent = getIntent();
        uuidEdit = intent.getStringExtra("uuidEdit");
        travelerEmailEdit = intent.getStringExtra("travelerEmailEdit");
        travelerFirstNameEdit = intent.getStringExtra("travelerFirstNameEdit");
        checkInDateTextEdit = intent.getStringExtra("checkInDateEdit");
        chekOutDateTextEdit = intent.getStringExtra("checkOutDateEdit");
        numberOfRoomsTextEdit = intent.getStringExtra("numberOfRoomsEdit");
        extraDetailsTextEdit = intent.getStringExtra("extraDetailsEdit");
        travelerContactNumberEdit = intent.getStringExtra("travelerContact");
        hotelOwnerEmailEdit = intent.getStringExtra("hotelOwnerEmail");
        hotelNameEdit = intent.getStringExtra("hotelName");
        hotelIdEdit = intent.getStringExtra("hotelId");


        //Creating calender
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //To select check in date
        checkInDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditBookHotel.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        checkInDateEdit.setText(date);
                    }
                }, year, month, day);
                //Disabling the past dates
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        //To select checkout date
        checkOutDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditBookHotel.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        checkOutDateEdit.setText(date);
                    }
                }, year, month, day);
                //Disabling the past dates
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        numberOfRoomsEdit.setAdapter(adapter);



        //setting current available data
        checkInDateEdit.setText(checkInDateTextEdit);
        checkOutDateEdit.setText(chekOutDateTextEdit);
        //numberOfRoomsEdit.setAdapter(numberOfRoomsTextEdit);
        extraDetailsEdit.setText(extraDetailsTextEdit);


        bookHotelSubmitButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting values from the fields and turn it to strings
                checkInDateTextEdit = checkInDateEdit.getText().toString();
                chekOutDateTextEdit = checkOutDateEdit.getText().toString();
                numberOfRoomsTextEdit = numberOfRoomsEdit.getSelectedItem().toString();
                extraDetailsTextEdit = extraDetailsEdit.getText().toString();

                HotelBookings hotelBookings = new HotelBookings(hotelNameEdit, hotelIdEdit, uuidEdit,travelerEmailEdit, hotelOwnerEmailEdit,travelerContactNumberEdit,
                        travelerFirstNameEdit, checkInDateTextEdit, chekOutDateTextEdit, numberOfRoomsTextEdit, extraDetailsTextEdit);
                databaseReference.child("Hotel Bookings").child(uuidEdit).setValue(hotelBookings).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditBookHotel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent1 = new Intent(EditBookHotel.this, TravelerAllBookings.class);
                startActivity(intent1);
                finish();

            }
        });

    }
}