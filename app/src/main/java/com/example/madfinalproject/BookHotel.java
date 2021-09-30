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

import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class BookHotel extends AppCompatActivity {

    Spinner numberOfRooms;
    ImageView backImage;
    TextView title;
    EditText extraDetails;
    Button checkInDate, checkOutDate, bookHotelSubmitButton;
    int year, month, day, numberOfDays, reservationFee;
    AlertDialog.Builder builder;
    String checkInDateText, chekOutDateText, numberOfRoomsText, extraDetailsText, travelerEmail, travelerFirstName, travelerContactNumber, hotelName,
            hotelId, uuid, hotelOwnerEmail, date1, date2, alertLine1, alertLine2;

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
                        date1 = dayOfMonth + "/" + month + "/" + year;
                        checkInDate.setText(date1);
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
                        date2 = dayOfMonth + "/" + month + "/" + year;
                        checkOutDate.setText(date2);
                    }
                }, year, month, day);

                //disabling the date selected before
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                long getCheckInDate = 0;
                try {
                    Date date = simpleDateFormat.parse(date1);
                    getCheckInDate = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.getDatePicker().setMinDate(getCheckInDate);
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
                            builder.setTitle("Alert!!").setMessage("You must make the payments to reserve " + hotelName)
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

                    //calculating the number of dates
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date date1 = simpleDateFormat.parse(checkInDateText);
                        Date date2 = simpleDateFormat.parse(chekOutDateText);

                        long TrCheckInDate = date1.getTime();
                        long TrCheckOutDate = date2.getTime();

                        if (TrCheckInDate <= TrCheckOutDate) {
                            Period period = new Period(TrCheckInDate, TrCheckOutDate, PeriodType.days());
                            numberOfDays = period.getDays();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //calculating the reservation fee
                    if(numberOfDays == 0){
                        reservationFee = Integer.parseInt(numberOfRoomsText) * 2 + 4;
                        alertLine1 = "You have selected " + numberOfRoomsText + " room(s) for 1 day in " + hotelName + ".";
                        alertLine2 = "This will cost you $ " + reservationFee + ".00";
                    } else {
                        reservationFee = Integer.parseInt(numberOfRoomsText) * 2 + numberOfDays * 4;
                        alertLine1 = "You have selected " + numberOfRoomsText + " room(s) for " + numberOfDays + " days in " + hotelName + ".";
                        alertLine2 = "This will cost you $ " + reservationFee + ".00";
                    }

                    new AlertDialog.Builder(BookHotel.this)
                            .setTitle("Hi " + travelerFirstName + ",")
                            .setMessage(alertLine1 + "\n" + alertLine2 + "\n" + "Are you sure that you want to continue?")
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

                                    PayPalPayment payment = new PayPalPayment(new BigDecimal(reservationFee), "USD", hotelName + " Reservation Fee", PayPalPayment.PAYMENT_INTENT_SALE);
                                    Intent intent = new Intent(BookHotel.this, PaymentActivity.class);
                                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
                                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                                    onStartActivityResultLauncherForPaymentGateway.launch(intent);

                                }
                            })
                            .show();
                }
            }
        });
    }


    //Creating book hotel method (for update the database)
    private void bookHotel() {
        HotelBookings hotelBookings = new HotelBookings(hotelName, hotelId, uuid, travelerEmail, hotelOwnerEmail, travelerContactNumber, travelerFirstName, checkInDateText,
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
    private void showNotification() {

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