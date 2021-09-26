package com.example.madfinalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;


public class AddHotel extends AppCompatActivity {

    DatabaseReference databaseReference;
    StorageReference storageReference;

    ArrayList<Uri> imageURIs = new ArrayList<>();

    TextView title;
    EditText hotelName, hotelAddress, hotelContact, hotelDescription, hotelCity;
    ImageView imageBack, hotelImg1, hotelImg2, hotelImg3, hotelImg4, hotelImg5;
    Button hotelImgUploadBtn, hotelSubmitBtn;
    Chip chipPetFriendly, chipFreeParking, chipBar, chipWifi, chipYoga, chipGym,
            chipSpa, chipSalon, chipRestaurant, chipPool, chipCoffeeShop, chipAtm, chipSnackBar;

    RelativeLayout mainLayout, progressBarLayout;

    //Creating the paypal configurations
    private final static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(PayPal.paypalClientID);

    String uuid, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);

        //generating a UUID for hotel id
        uuid = UUID.randomUUID().toString();

        //getting the current hotel owner
        SessionsHotelOwner hotelOwner = new SessionsHotelOwner(this);

        HashMap<String, String> hotelOwnerDetails = hotelOwner.getHotelOwnerDetailsFromSessions();
        email = hotelOwnerDetails.get(SessionsHotelOwner.KEY_EMAIL);

        //Starting the paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);

        title = findViewById(R.id.actionBar);
        imageBack = findViewById(R.id.imageBack);

        hotelImgUploadBtn = findViewById(R.id.hotelImgUploadBtn);
        hotelSubmitBtn = findViewById(R.id.hotelSubmitBtn);

        hotelName = findViewById(R.id.hotelName);
        hotelAddress = findViewById(R.id.hotelAddress);
        hotelContact = findViewById(R.id.hotelContact);
        hotelDescription = findViewById(R.id.hotelDescription);
        hotelCity = findViewById(R.id.hotelCity);

        hotelImg1 = findViewById(R.id.hotelImg1);
        hotelImg2 = findViewById(R.id.hotelImg2);
        hotelImg3 = findViewById(R.id.hotelImg3);
        hotelImg4 = findViewById(R.id.hotelImg4);
        hotelImg5 = findViewById(R.id.hotelImg5);

        chipPetFriendly = findViewById(R.id.chipPetFriendly);
        chipFreeParking = findViewById(R.id.chipFreeParking);
        chipBar = findViewById(R.id.chipBar);
        chipWifi = findViewById(R.id.chipWifi);
        chipYoga = findViewById(R.id.chipYoga);
        chipGym = findViewById(R.id.chipGym);
        chipSpa = findViewById(R.id.chipSpa);
        chipSalon = findViewById(R.id.chipSalon);
        chipPool = findViewById(R.id.chipPool);
        chipAtm = findViewById(R.id.chipAtm);
        chipSnackBar = findViewById(R.id.chipSnackBar);
        chipRestaurant = findViewById(R.id.chipRestaurant);
        chipCoffeeShop = findViewById(R.id.chipCoffeeShop);

        mainLayout = findViewById(R.id.mainLayout);
        progressBarLayout = findViewById(R.id.progressBarLayout);

        //setting the activity title
        title.setText(R.string.hotel_activity_topic);

        //setting the onClick listener for the back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHotel.super.onBackPressed();
            }
        });

        //creating the activity launcher method to launch the onResult activity for image upload
        ActivityResultLauncher<Intent> onStartActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            assert result.getData() != null;
                            ClipData clipData = result.getData().getClipData();

                            //if multiple images were selected
                            if (clipData != null) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri imageUri = clipData.getItemAt(i).getUri();
                                    imageURIs.add(imageUri);
                                }
                            } else {
                                Uri imageUri = result.getData().getData();
                                imageURIs.add(imageUri);
                            }

                            //to reset all image views to visibility GONE
                            hotelImg1.setVisibility(View.GONE);
                            hotelImg2.setVisibility(View.GONE);
                            hotelImg3.setVisibility(View.GONE);
                            hotelImg4.setVisibility(View.GONE);
                            hotelImg5.setVisibility(View.GONE);

                            if (imageURIs.size() > 5) {
                                imageURIs.clear();

                                new AlertDialog.Builder(AddHotel.this).setTitle("Alert!!").setMessage("Only 5 Images Can be Selected!")
                                        .setCancelable(true)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .show();
                            } else {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //to update the ui from a non UI Thread
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //toggling image views and displaying images as necessary
                                                if (imageURIs.size() == 1) {
                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                } else if (imageURIs.size() == 2) {
                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(hotelImg2);

                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                    hotelImg2.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 3) {
                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(hotelImg2);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(hotelImg3);


                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                    hotelImg2.setVisibility(View.VISIBLE);
                                                    hotelImg3.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 4) {
                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(hotelImg2);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(hotelImg3);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(3))
                                                            .into(hotelImg4);

                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                    hotelImg2.setVisibility(View.VISIBLE);
                                                    hotelImg3.setVisibility(View.VISIBLE);
                                                    hotelImg4.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 5) {
                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(hotelImg2);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(hotelImg3);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(3))
                                                            .into(hotelImg4);

                                                    Glide.with(AddHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(4))
                                                            .into(hotelImg5);

                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                    hotelImg2.setVisibility(View.VISIBLE);
                                                    hotelImg3.setVisibility(View.VISIBLE);
                                                    hotelImg4.setVisibility(View.VISIBLE);
                                                    hotelImg5.setVisibility(View.VISIBLE);

                                                }
                                            }
                                        });
                                    }
                                }).start();


                            }
                        }

                    }

                });


        //setting the onClick listener for image upload button
        hotelImgUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking whether the permission is granted or not
                if (ActivityCompat.checkSelfPermission(AddHotel.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //requesting user to accept the external storage permission
                    ActivityCompat.requestPermissions(AddHotel.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                imageURIs.clear();
                System.out.println(imageURIs);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                //invoking the launcher method
                onStartActivityResultLauncher.launch(intent);

            }
        });

        //activity launcher for payment gateway
        ActivityResultLauncher<Intent> onStartActivityResultLauncherForPaymentGateway = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Toast.makeText(AddHotel.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                            uploadHotel();
                        } else {
                            Toast.makeText(AddHotel.this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //creating the submit handler
        hotelSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(10), "USD", "Hotel Registration Payment", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(AddHotel.this, PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                onStartActivityResultLauncherForPaymentGateway.launch(intent);
            }


        });

    }

    private String getImgExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void uploadHotel(){
                        mainLayout.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.VISIBLE);

                databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");
                storageReference = FirebaseStorage.getInstance().getReference();

                ArrayList<String> amenities = new ArrayList<>();

                if (chipPetFriendly.isChecked()) {
                    amenities.add("Pet Friendly");
                }
                if (chipFreeParking.isChecked()) {
                    amenities.add("Free Parking");
                }
                if (chipBar.isChecked()) {
                    amenities.add("Bar");
                }
                if (chipWifi.isChecked()) {
                    amenities.add("Wi-Fi");
                }
                if (chipYoga.isChecked()) {
                    amenities.add("Yoga");
                }
                if (chipGym.isChecked()) {
                    amenities.add("Gym");
                }
                if (chipSpa.isChecked()) {
                    amenities.add("Spa");
                }
                if (chipSalon.isChecked()) {
                    amenities.add("Salon");
                }
                if (chipRestaurant.isChecked()) {
                    amenities.add("Restaurant");
                }
                if (chipPool.isChecked()) {
                    amenities.add("Pool");
                }
                if (chipCoffeeShop.isChecked()) {
                    amenities.add("Coffee Shop");
                }
                if (chipAtm.isChecked()) {
                    amenities.add("ATM");
                }
                if (chipSnackBar.isChecked()) {
                    amenities.add("Snack Bar");
                }

                Hotel hotel = new Hotel();

                hotel.setName(hotelName.getText().toString());
                hotel.setOwner(email);
                hotel.setAddress((hotelAddress.getText().toString()));
                hotel.setContact(hotelContact.getText().toString());
                hotel.setDescription((hotelDescription.getText().toString()));
                hotel.setCity(hotelCity.getText().toString());
                hotel.setAmenities((amenities));
                hotel.setHotelId(uuid);

                databaseReference.child("Hotels").child(uuid).setValue(hotel).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddHotel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //uploading images to fire based storage
                if (imageURIs.size() != 0) {
                    for (int i = 0; i < imageURIs.size(); i++) {
                        String uuidForImg = UUID.randomUUID().toString();
                        StorageReference newStorageRef = storageReference.child("Hotel_Images").child(uuidForImg + "." + getImgExtension(imageURIs.get(i)));

                        newStorageRef.putFile(imageURIs.get(i))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String id = UUID.randomUUID().toString();

                                        //without onSuccess listener URI cannot be grabbed as the method id async
                                        //Objects.requireNonNull is used to handle Null Pointer exception as getReference and getDownloadUrl could throw them.
                                        Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                databaseReference
                                                        .child("Hotels")
                                                        .child(uuid).child("images")
                                                        .child(id)
                                                        .setValue(uri.toString());
                                            }
                                        });

                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddHotel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AddHotel.this, HotelOwnerMainView.class);
                        startActivity(intent);
                        finish();
                    }
                }, 20000);
    }

    @Override
    protected void onDestroy(){
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}