package com.example.madfinalproject;

import static com.example.madfinalproject.NotificationHelper.ChannelId;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;


public class BookHotel extends AppCompatActivity {

    Spinner numberOfRooms;
    ImageView backImage;
    TextView title;
    EditText extraDetails;
    Button checkInDate, checkOutDate, bookHotelSubmitButton;
    int year, month, day;
    AlertDialog.Builder builder;
    String checkInDateText, chekOutDateText, numberOfRoomsText, extraDetailsText, travelerEmail, travelerFirstName, travelerContactNumber, hotelName,
            hotelId, uuid, hotelOwnerEmail;

    //Creating object to access firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");

    NotificationManagerCompat notificationManager;

    //Creating the paypal configurations
    private final static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(PayPal.paypalClientID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_hotel);


        numberOfRooms = findViewById(R.id.numberOfRooms);
        backImage = findViewById(R.id.imageBack);
        title = findViewById(R.id.actionBar);
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        extraDetails = findViewById(R.id.bookHotelExtraDetails);
        bookHotelSubmitButton = findViewById(R.id.bookHotelSubmitButton);


        //Getting the hotel id and hotel name
        Intent intent1 = getIntent();
        hotelId = intent1.getStringExtra("HotelId");
        hotelName = intent1.getStringExtra("hotelName");
        hotelOwnerEmail = intent1.getStringExtra("hotelOwnerEmail");



        builder = new AlertDialog.Builder(this);//Creating the dialog object

        notificationManager = NotificationManagerCompat.from(this);//Creating notificationManagerCompat object

        //getting the current traveler
        SessionsTraveler traveler = new SessionsTraveler(this);

        HashMap<String, String> travelerDetails = traveler.getTravelerDetailsFromSessions();
        travelerEmail = travelerDetails.get(SessionsTraveler.KEY_EMAIL);
        travelerFirstName = travelerDetails.get(SessionsTraveler.KEY_FIRSTNAME);
        travelerContactNumber = travelerDetails.get(SessionsTraveler.KEY_CONTACTNUMBER);

        //Starting the paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);

        //Setting page title
        title.setText("Book Hotel");

        //Creating uuid
        uuid = UUID.randomUUID().toString();

        //Setup a onclick listener for the back image
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookHotel.super.onBackPressed();
            }
        });

        //Creating calender
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //To select check in date
        checkInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookHotel.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        checkInDate.setText(date);
                    }
                }, year, month, day);
                //Disabling the past dates
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        //To select checkout date
        checkOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookHotel.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        checkOutDate.setText(date);
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
        numberOfRooms.setAdapter(adapter);

        //activity launcher for payment gateway
        ActivityResultLauncher<Intent> onStartActivityResultLauncherForPaymentGateway = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Toast.makeText(BookHotel.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                            bookHotel();
                        } else {
                            //Telling user that they must pay the fee to reserve hotel
                            builder.setTitle("Alert!!").setMessage("You must make the payments to reserve this hotel")
                                    .setCancelable(true)
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .show();
                        }
                    }
                });

        //To submit handler
        bookHotelSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting values from the fields and turn it to strings
                checkInDateText = checkInDate.getText().toString();
                chekOutDateText = checkOutDate.getText().toString();
                numberOfRoomsText = numberOfRooms.getSelectedItem().toString();
                extraDetailsText = extraDetails.getText().toString();


                //calculating the reservation fee
                int  reservationFee = Integer.parseInt(numberOfRoomsText) * 4;

                if (checkInDateText.isEmpty() || chekOutDateText.isEmpty()) {

                    //Telling user that they must fill the fields
                    builder.setTitle("Alert!!").setMessage("Please fill the fields")
                            .setCancelable(true)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                } else {
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(reservationFee), "USD", hotelName +" Hotel Reservation Fee", PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(BookHotel.this, PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                    onStartActivityResultLauncherForPaymentGateway.launch(intent);
                }
            }
        });
    }


    //Creating book hotel method (for update the database)
    private void bookHotel() {
        HotelBookings hotelBookings = new HotelBookings(hotelName, hotelId, uuid,travelerEmail, hotelOwnerEmail,travelerContactNumber, travelerFirstName, checkInDateText,
                chekOutDateText, numberOfRoomsText, extraDetailsText);
        databaseReference.child("Hotel Bookings").child(uuid).setValue(hotelBookings).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BookHotel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        showNotification();
        Intent intent2 = new Intent(this, TravelerMainView.class);
        startActivity(intent2);
        finish();
    }


    //Creating the notification
    private void showNotification(){

        String title = "Hi " + travelerFirstName;
        String body = "You have successfully reserved " + hotelName + " form " + checkInDate + "to " + checkOutDate;

        //Redirect to the traveler all bookings activity
        Intent intent = new Intent(this, TravelerAllBookings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, ChannelId)
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);

    }

}