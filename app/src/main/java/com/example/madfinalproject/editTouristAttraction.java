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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class editTouristAttraction extends AppCompatActivity {

    DatabaseReference databaseReference;
    StorageReference storageReference;

    ArrayList<Uri> imageURIs = new ArrayList<>();

    TextView title;
    EditText placeName, placeAddress, placeDescription, placeCity;
    ImageView imageBack, placeImg1, placeImg2, placeImg3, placeImg4, placeImg5;
    Button placeSubmitBtn, placeImgUploadBtn;

    RelativeLayout mainLayout, progressBarLayout;

    AttractionPlaces attractionPlaces = new AttractionPlaces();
    AttractionPlaces currentPlaceData = new AttractionPlaces();
    Boolean imgEditStatus = false;

    String uuid, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tourist_attraction);

        //getting the current tour guide
        SessionsTourGuide tourGuide = new SessionsTourGuide(this);

        HashMap<String, String> tourGuideDetails = tourGuide.getTourGuideDetailsFromSessions();
        email = tourGuideDetails.get(SessionsTourGuide.KEY_EMAIL);

        //getting the current data
        currentPlaceData = (AttractionPlaces) getIntent().getSerializableExtra("placeObj");

        title = findViewById(R.id.actionBar);
        imageBack = findViewById(R.id.imageBack);

        placeImgUploadBtn = findViewById(R.id.placeImgUploadBtn);
        placeSubmitBtn = findViewById(R.id.placeSubmitBtn);

        placeName = findViewById(R.id.placeName);
        placeAddress = findViewById(R.id.placeAddress);
        placeDescription = findViewById(R.id.placeDescription);
        placeCity = findViewById(R.id.placeCity);

        placeImg1 = findViewById(R.id.placeImg1);
        placeImg2 = findViewById(R.id.placeImg2);
        placeImg3 = findViewById(R.id.placeImg3);
        placeImg4 = findViewById(R.id.placeImg4);
        placeImg5 = findViewById(R.id.placeImg5);

        mainLayout = findViewById(R.id.mainLayout);
        progressBarLayout = findViewById(R.id.progressBarLayout);

        //setting the activity title
        title.setText(R.string.editTouristAttraction);

        //setting the onClick listener for the back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { editTouristAttraction.super.onBackPressed();
            }
        });

        //setting data to the inputs
        //getting placeId
        uuid = currentPlaceData.getPlaceId();
        //details
        placeName.setText(currentPlaceData.getName());
        placeAddress.setText(currentPlaceData.getAddress());
        placeDescription.setText(currentPlaceData.getDescription());
        placeCity.setText(currentPlaceData.getCity());


        //images
        for(Map.Entry<String, String> entry: currentPlaceData.getImages().entrySet()){
            imageURIs.add(Uri.parse(entry.getValue()));

        }

        //rendering images
        if(imageURIs.size() == currentPlaceData.getImages().size()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //to update the ui from a non UI Thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //toggling image views and displaying images as necessary
                            if (imageURIs.size() == 1) {
                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(placeImg1);

                                placeImg1.setVisibility(View.VISIBLE);
                            } else if (imageURIs.size() == 2) {
                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(placeImg1);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(1))
                                        .into(placeImg2);

                                placeImg1.setVisibility(View.VISIBLE);
                                placeImg2.setVisibility(View.VISIBLE);

                            } else if (imageURIs.size() == 3) {
                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(placeImg1);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(1))
                                        .into(placeImg2);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(2))
                                        .into(placeImg3);


                                placeImg1.setVisibility(View.VISIBLE);
                                placeImg2.setVisibility(View.VISIBLE);
                                placeImg3.setVisibility(View.VISIBLE);

                            } else if (imageURIs.size() == 4) {
                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(placeImg1);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(1))
                                        .into(placeImg2);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(2))
                                        .into(placeImg3);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(3))
                                        .into(placeImg4);

                                placeImg1.setVisibility(View.VISIBLE);
                                placeImg2.setVisibility(View.VISIBLE);
                                placeImg3.setVisibility(View.VISIBLE);
                                placeImg4.setVisibility(View.VISIBLE);

                            } else if (imageURIs.size() == 5) {
                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(0))
                                        .into(placeImg1);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(1))
                                        .into(placeImg2);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(2))
                                        .into(placeImg3);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(3))
                                        .into(placeImg4);

                                Glide.with(editTouristAttraction.this)
                                        .asBitmap()
                                        .load(imageURIs.get(4))
                                        .into(placeImg5);

                                placeImg1.setVisibility(View.VISIBLE);
                                placeImg2.setVisibility(View.VISIBLE);
                                placeImg3.setVisibility(View.VISIBLE);
                                placeImg4.setVisibility(View.VISIBLE);
                                placeImg5.setVisibility(View.VISIBLE);

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
                            }else{
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri imageUri = clipData.getItemAt(i).getUri();
                                    imageURIs.add(imageUri);
                                }
                                imgEditStatus = true;
                            }

                            //to reset all image views to visibility GONE
                            placeImg1.setVisibility(View.GONE);
                            placeImg2.setVisibility(View.GONE);
                            placeImg3.setVisibility(View.GONE);
                            placeImg4.setVisibility(View.GONE);
                            placeImg5.setVisibility(View.GONE);

                            if (imageURIs.size() > 5) {
                                imageURIs.clear();

                                new AlertDialog.Builder(editTouristAttraction.this).setTitle("Alert!!").setMessage("Only 5 Images Can be Selected!")
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
                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    placeImg1.setVisibility(View.VISIBLE);
                                                } else if (imageURIs.size() == 2) {
                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(placeImg2);

                                                    placeImg1.setVisibility(View.VISIBLE);
                                                    placeImg2.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 3) {
                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(placeImg2);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(placeImg3);


                                                    placeImg1.setVisibility(View.VISIBLE);
                                                    placeImg2.setVisibility(View.VISIBLE);
                                                    placeImg3.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 4) {
                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(placeImg2);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(placeImg3);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(3))
                                                            .into(placeImg4);

                                                    placeImg1.setVisibility(View.VISIBLE);
                                                    placeImg2.setVisibility(View.VISIBLE);
                                                    placeImg3.setVisibility(View.VISIBLE);
                                                    placeImg4.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 5) {
                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(placeImg2);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(placeImg3);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(3))
                                                            .into(placeImg4);

                                                    Glide.with(editTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(4))
                                                            .into(placeImg5);

                                                    placeImg1.setVisibility(View.VISIBLE);
                                                    placeImg2.setVisibility(View.VISIBLE);
                                                    placeImg3.setVisibility(View.VISIBLE);
                                                    placeImg4.setVisibility(View.VISIBLE);
                                                    placeImg5.setVisibility(View.VISIBLE);

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
        placeImgUploadBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //checking whether the permission is granted or not
            if (ActivityCompat.checkSelfPermission(editTouristAttraction.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //requesting user to accept the external storage permission
                ActivityCompat.requestPermissions(editTouristAttraction.this,
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
        placeSubmitBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) { uploadPlace(); }


    });

}

    private String getImgExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void uploadPlace(){
        mainLayout.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");
        storageReference = FirebaseStorage.getInstance().getReference();

        attractionPlaces.setName(placeName.getText().toString());
        attractionPlaces.setTourGuide(email);
        attractionPlaces.setAddress((placeAddress.getText().toString()));
        attractionPlaces.setDescription((placeDescription.getText().toString()));
        attractionPlaces.setCity(placeCity.getText().toString());
        attractionPlaces.setPlaceId(uuid);

        databaseReference.child("Places").child(uuid).setValue(attractionPlaces).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editTouristAttraction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(imgEditStatus) {

            //uploading images to fire based storage
            if (imageURIs.size() != 0) {
                for (int i = 0; i < imageURIs.size(); i++) {
                    String uuidForImg = UUID.randomUUID().toString();
                    StorageReference newStorageRef = storageReference.child("Place_Images").child(uuidForImg + "." + getImgExtension(imageURIs.get(i)));

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
                                                    .child("Places")
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
                                    Toast.makeText(editTouristAttraction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }

            //deleting existing images
            StorageReference imageRef;
            for (Map.Entry<String, String> entry : currentPlaceData.getImages().entrySet()) {
                imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(entry.getValue());
                imageRef.delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editTouristAttraction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else {
            //resetting downloadable URLs
            databaseReference
                    .child("Places")
                    .child(uuid).child("images")
                    .setValue(currentPlaceData.getImages());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(editTouristAttraction.this, tourguideSingleView.class);
                intent.putExtra("placeId", uuid);
                startActivity(intent);
                finish();
            }
        }, 20000);
    }

}