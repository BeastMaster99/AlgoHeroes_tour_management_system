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
//import android.widget.TextView;
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
import java.util.Objects;
import java.util.UUID;

public class addTouristAttraction extends AppCompatActivity {

    DatabaseReference databaseReference;
    StorageReference storageReference;

    ArrayList<Uri> imageURIs = new ArrayList<>();

    EditText textView11, textView12, textView13, textView19;
    ImageView imageView6,  placeImg1, placeImg2, placeImg3, placeImg4, placeImg5;
    Button button6, button10;

    String uuid, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_tourist_attraction);

        //Generating a UUID for place id
        uuid = UUID.randomUUID().toString();

        //getting the current tour guide
        SessionsTourGuide tourGuide = new SessionsTourGuide(this);

        HashMap<String, String> tourGuideDetails = tourGuide.getTourGuideDetailsFromSessions();
        email = tourGuideDetails.get(SessionsTourGuide.KEY_EMAIL);


        imageView6 = findViewById(R.id.imageView6);

        button10 = findViewById(R.id.button10);
        button6 = findViewById(R.id.button6);

        textView11 = findViewById(R.id.textView11);
        textView12 = findViewById(R.id.textView12);
        textView13 = findViewById(R.id.textView13);
        textView19 = findViewById(R.id.textView19);

        placeImg1 = findViewById(R.id.placeImg1);
        placeImg2 = findViewById(R.id.placeImg2);
        placeImg3 = findViewById(R.id.placeImg3);
        placeImg4 = findViewById(R.id.placeImg4);
        placeImg5 = findViewById(R.id.placeImg5);

        //setting the onClick listener for the back button
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTouristAttraction.super.onBackPressed();
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
                            placeImg1.setVisibility(View.GONE);
                            placeImg2.setVisibility(View.GONE);
                            placeImg3.setVisibility(View.GONE);
                            placeImg4.setVisibility(View.GONE);
                            placeImg5.setVisibility(View.GONE);

                            if (imageURIs.size() > 5) {
                                imageURIs.clear();

                                new AlertDialog.Builder(addTouristAttraction.this).setTitle("Alert!!").setMessage("Only 5 Images Can be Selected!")
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
                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    placeImg1.setVisibility(View.VISIBLE);
                                                } else if (imageURIs.size() == 2) {
                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(placeImg2);

                                                    placeImg1.setVisibility(View.VISIBLE);
                                                    placeImg2.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 3) {
                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(placeImg2);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(placeImg3);


                                                    placeImg1.setVisibility(View.VISIBLE);
                                                    placeImg2.setVisibility(View.VISIBLE);
                                                    placeImg3.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 4) {
                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(placeImg2);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(placeImg3);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(3))
                                                            .into(placeImg4);

                                                    placeImg1.setVisibility(View.VISIBLE);
                                                    placeImg2.setVisibility(View.VISIBLE);
                                                    placeImg3.setVisibility(View.VISIBLE);
                                                    placeImg4.setVisibility(View.VISIBLE);

                                                } else if (imageURIs.size() == 5) {
                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(0))
                                                            .into(placeImg1);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(1))
                                                            .into(placeImg2);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(2))
                                                            .into(placeImg3);

                                                    Glide.with(addTouristAttraction.this)
                                                            .asBitmap()
                                                            .load(imageURIs.get(3))
                                                            .into(placeImg4);

                                                    Glide.with(addTouristAttraction.this)
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
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking whether the permission is granted or not
                if (ActivityCompat.checkSelfPermission(addTouristAttraction.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //requesting user to accept the external storage permission
                    ActivityCompat.requestPermissions(addTouristAttraction.this,
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

        //setting the on click listener to upload button
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPlaces();
            }
        });



    }

    //aaa
    private String getImgExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void uploadPlaces(){


        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");
        storageReference = FirebaseStorage.getInstance().getReference();



        AttractionPlaces attractionPlaces = new AttractionPlaces();

        attractionPlaces.setPlaceId(uuid);
        attractionPlaces.setName(textView11.getText().toString());
        attractionPlaces.setTourGuide(email);
        attractionPlaces.setAddress((textView12.getText().toString()));
        attractionPlaces.setDescription((textView19.getText().toString()));
        attractionPlaces.setCity(textView13.getText().toString());


        databaseReference.child("Places").child(uuid).setValue(attractionPlaces).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addTouristAttraction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
                                Toast.makeText(addTouristAttraction.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(addTouristAttraction.this, TourGuideMainView.class);
                startActivity(intent);
            }
        }, 20000);
    }


}