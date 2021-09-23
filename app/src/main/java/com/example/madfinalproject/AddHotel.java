package com.example.madfinalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class AddHotel extends AppCompatActivity {

    ArrayList<Uri> imageURIs = new ArrayList<>();

    TextView title;
    ImageView imageBack, hotelImg1, hotelImg2, hotelImg3, hotelImg4, hotelImg5;
    Button hotelImgUploadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);

        title = findViewById(R.id.actionBar);
        imageBack = findViewById(R.id.imageBack);
        hotelImgUploadBtn = findViewById(R.id.hotelImgUploadBtn);

        hotelImg1 = findViewById(R.id.hotelImg1);
        hotelImg2 = findViewById(R.id.hotelImg2);
        hotelImg3 = findViewById(R.id.hotelImg3);
        hotelImg4 = findViewById(R.id.hotelImg4);
        hotelImg5 = findViewById(R.id.hotelImg5);

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

    }

}