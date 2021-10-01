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
        import com.paypal.android.sdk.payments.PayPalService;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Objects;
        import java.util.UUID;


public class EditHotel extends AppCompatActivity {

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

    Hotel hotel = new Hotel();
    Hotel currentHotelData = new Hotel();
    Boolean imgEditStatus = false;

    //Creating the paypal configurations
    private final static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(PayPal.paypalClientID);

    String uuid, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hotel);

        //getting the current hotel owner
        SessionsHotelOwner hotelOwner = new SessionsHotelOwner(this);

        HashMap<String, String> hotelOwnerDetails = hotelOwner.getHotelOwnerDetailsFromSessions();
        email = hotelOwnerDetails.get(SessionsHotelOwner.KEY_EMAIL);

        //Starting the paypal service
        Intent intent = new Intent(this, PayPalService.class);
        startService(intent);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        //getting the current data
        currentHotelData = (Hotel) getIntent().getSerializableExtra("hotelObj");

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
                EditHotel.super.onBackPressed();
            }
        });

        //setting data to the inputs
        //getting hotelId
        uuid = currentHotelData.getHotelId();
        //details
        hotelName.setText(currentHotelData.getName());
        hotelAddress.setText(currentHotelData.getAddress());
        hotelContact.setText((currentHotelData.getContact()));
        hotelDescription.setText(currentHotelData.getDescription());
        hotelCity.setText(currentHotelData.getCity());

        //chips
        if (currentHotelData.getAmenities().contains("Pet Friendly")) {
            chipPetFriendly.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Free Parking")) {
            chipFreeParking.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Bar")) {
            chipBar.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Wi-Fi")) {
            chipWifi.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Yoga")) {
            chipYoga.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Gym")) {
            chipGym.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Spa")) {
            chipSpa.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Salon")) {
            chipSalon.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Restaurant")) {
            chipRestaurant.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Pool")) {
            chipPool.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Coffee Shop")) {
            chipCoffeeShop.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("ATM")) {
            chipAtm.setChecked(true);
        }
        if (currentHotelData.getAmenities().contains("Snack Bar")) {
            chipSnackBar.setChecked(true);
        }

        //images
        for(Map.Entry<String, String> entry: currentHotelData.getImages().entrySet()){
            imageURIs.add(Uri.parse(entry.getValue()));

        }

        //rendering images
        if(imageURIs.size() == currentHotelData.getImages().size()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //to update the ui from a non UI Thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //toggling image views and displaying images as necessary
                            if (imageURIs.size() == 1) {
                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(hotelImg1);

                                hotelImg1.setVisibility(View.VISIBLE);
                            } else if (imageURIs.size() == 2) {
                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(hotelImg1);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(1))
                                        .into(hotelImg2);

                                hotelImg1.setVisibility(View.VISIBLE);
                                hotelImg2.setVisibility(View.VISIBLE);

                            } else if (imageURIs.size() == 3) {
                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(hotelImg1);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(1))
                                        .into(hotelImg2);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(2))
                                        .into(hotelImg3);


                                hotelImg1.setVisibility(View.VISIBLE);
                                hotelImg2.setVisibility(View.VISIBLE);
                                hotelImg3.setVisibility(View.VISIBLE);

                            } else if (imageURIs.size() == 4) {
                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(hotelImg1);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(1))
                                        .into(hotelImg2);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(2))
                                        .into(hotelImg3);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(3))
                                        .into(hotelImg4);

                                hotelImg1.setVisibility(View.VISIBLE);
                                hotelImg2.setVisibility(View.VISIBLE);
                                hotelImg3.setVisibility(View.VISIBLE);
                                hotelImg4.setVisibility(View.VISIBLE);

                            } else if (imageURIs.size() == 5) {
                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(hotelImg1);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(1))
                                        .into(hotelImg2);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(2))
                                        .into(hotelImg3);

                                Glide.with(EditHotel.this)
                                        .asBitmap()
                                        .load(imageURIs.get(3))
                                        .into(hotelImg4);

                                Glide.with(EditHotel.this)
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

        //creating the activity launcher method to launch the onResult activity for image upload
        ActivityResultLauncher<Intent> onStartActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            assert result.getData() != null;
                            ClipData clipData = result.getData().getClipData();

                            imageURIs.clear();

                            //if multiple images were selected
                            if (clipData == null) {
                                Uri imageUri = result.getData().getData();
                                imageURIs.add(imageUri);
                            } else {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri imageUri = clipData.getItemAt(i).getUri();
                                    imageURIs.add(imageUri);
                                }
                                imgEditStatus = true;
                            }

                            //to reset all image views to visibility GONE
                            hotelImg1.setVisibility(View.GONE);
                            hotelImg2.setVisibility(View.GONE);
                            hotelImg3.setVisibility(View.GONE);
                            hotelImg4.setVisibility(View.GONE);
                            hotelImg5.setVisibility(View.GONE);

                            if (imageURIs.size() > 5) {
                                imageURIs.clear();

                                new AlertDialog.Builder(EditHotel.this).setTitle("Alert!!").setMessage("Only 5 Images Can be Selected!")
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
                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                } else if (imageURIs.size() == 2) {
                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(hotelImg2);

                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                    hotelImg2.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 3) {
                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(hotelImg2);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(hotelImg3);


                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                    hotelImg2.setVisibility(View.VISIBLE);
                                                    hotelImg3.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 4) {
                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(hotelImg2);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(hotelImg3);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(3))
                                                            .into(hotelImg4);

                                                    hotelImg1.setVisibility(View.VISIBLE);
                                                    hotelImg2.setVisibility(View.VISIBLE);
                                                    hotelImg3.setVisibility(View.VISIBLE);
                                                    hotelImg4.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 5) {
                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(hotelImg1);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(hotelImg2);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(hotelImg3);

                                                    Glide.with(EditHotel.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(3))
                                                            .into(hotelImg4);

                                                    Glide.with(EditHotel.this)
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
                if (ActivityCompat.checkSelfPermission(EditHotel.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //requesting user to accept the external storage permission
                    ActivityCompat.requestPermissions(EditHotel.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }

                System.out.println(imageURIs);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                //invoking the launcher method
                onStartActivityResultLauncher.launch(intent);

            }
        });



        //creating the submit handler
        hotelSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation - if fields are empty
                if (hotelName.getText().toString().isEmpty() ||
                        hotelAddress.getText().toString().isEmpty() ||
                        hotelContact.getText().toString().isEmpty() ||
                        hotelDescription.getText().toString().isEmpty() ||
                        hotelCity.getText().toString().isEmpty() ||
                        imageURIs.size() == 0) {

                    new AlertDialog.Builder(EditHotel.this).setTitle("Alert!!").setMessage("Please Fill All The Fields To Continue!")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();

                } else {
                    uploadHotel();
                }
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
                Toast.makeText(EditHotel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(imgEditStatus) {

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
                                    Toast.makeText(EditHotel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }

            //deleting existing images
            StorageReference imageRef;
            for (Map.Entry<String, String> entry : currentHotelData.getImages().entrySet()) {
                imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(entry.getValue());
                imageRef.delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditHotel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else {
            //resetting downloadable URLs
            databaseReference
                    .child("Hotels")
                    .child(uuid).child("images")
                    .setValue(currentHotelData.getImages());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(EditHotel.this, HotelHotelOwnerMainView.class);
                intent.putExtra("hotelId", uuid);
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