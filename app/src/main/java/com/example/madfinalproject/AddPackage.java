package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class AddPackage extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //StorageReference storageReference;

    //ArrayList<Uri> imageURIs = new ArrayList<>();

    TextView title;
    EditText Name, numberOfGuest, feePerDay, packageDescription, numRooms;
    Button packageSubmitBtn;
    Chip chipAirCondition, chipHouseKeeping, chipTel, chipSofa, chipDesk, chipSafe,
            chipMiniBar, chipRefrigerator, chipBathRooms, chipBottledWater, chipTV;
    Chip chipCityView, chipLandMark, chipFamilyRooms, chipNonSmokingRooms,
            chipSmokingRooms;
    //IconSwitch switch1;

    String uuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);


        //generating a UUID for package id
        uuid = UUID.randomUUID().toString();

        //title = findViewById(R.id.actionBar);

        packageSubmitBtn = findViewById(R.id.packageSubmitBtn);

        Name = findViewById(R.id.Name);
        numberOfGuest = findViewById(R.id.numberOfGuest);
        feePerDay = findViewById(R.id.feePerDay);
        packageDescription = findViewById(R.id.packageDescription);
        numRooms = findViewById(R.id.numRooms);

        chipAirCondition = findViewById(R.id.chipAirCondition);
        chipHouseKeeping = findViewById(R.id.chipHouseKeeping);
        chipTel = findViewById(R.id.chipTel);
        chipSofa = findViewById(R.id.chipSofa);
        chipDesk = findViewById(R.id.chipDesk);
        chipSafe = findViewById(R.id.chipSafe);
        chipMiniBar = findViewById(R.id.chipMiniBar);
        chipRefrigerator = findViewById(R.id.chipRefrigerator);
        chipBathRooms = findViewById(R.id.chipBathRooms);
        chipBottledWater = findViewById(R.id.chipBottledWater);
        chipTV = findViewById(R.id.chipTV);

        chipCityView = findViewById(R.id.chipCityView);
        chipLandMark = findViewById(R.id.chipLandMark);
        chipFamilyRooms = findViewById(R.id.chipFamilyRooms);
        chipNonSmokingRooms = findViewById(R.id.chipNonSmokingRooms);
        chipSmokingRooms = findViewById(R.id.chipSmokingRooms);

        //switch1 = findViewById(R.id.switch1);

        firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("Packages")

        packageSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");
                //storageReference = FirebaseStorage.getInstance().getReference();
                databaseReference = firebaseDatabase.getReference("Packages");

                ArrayList<String> amenities = new ArrayList<>();

                if (chipAirCondition.isChecked()) {
                    amenities.add("Air Condition");
                }
                if (chipHouseKeeping.isChecked()) {
                    amenities.add("House Keeping");
                }
                if (chipTel.isChecked()) {
                    amenities.add("Telephone");
                }
                if (chipSofa.isChecked()) {
                    amenities.add("Sofa");
                }
                if (chipDesk.isChecked()) {
                    amenities.add("Desk");
                }
                if (chipSafe.isChecked()) {
                    amenities.add("Safe");
                }
                if (chipMiniBar.isChecked()) {
                    amenities.add("Mini-Bar");
                }
                if (chipRefrigerator.isChecked()) {
                    amenities.add("Refrigerator");
                }
                if (chipBathRooms.isChecked()) {
                    amenities.add("Bath Rooms");
                }
                if (chipBottledWater.isChecked()) {
                    amenities.add("Bottled Water");
                }
                if (chipTV.isChecked()) {
                    amenities.add("TV");
                }

                ArrayList<String> amenities2 = new ArrayList<>();

                if (chipCityView.isChecked()) {
                    amenities.add("City View");
                }
                if (chipLandMark.isChecked()) {
                    amenities.add("Land Mark");
                }
                if (chipFamilyRooms.isChecked()) {
                    amenities.add("Family Rooms");
                }
                if (chipNonSmokingRooms.isChecked()) {
                    amenities.add("Non-Smoking Rooms");
                }
                if (chipSmokingRooms.isChecked()) {
                    amenities.add("Smoking Rooms");
                }

                String name = Name.getText().toString();
                String guest = numberOfGuest.getText().toString();
                String fee = feePerDay.getText().toString();
                String description =packageDescription.getText().toString();




                Package pkg = new Package(name, guest, fee, description,
                        amenities,amenities2,uuid);


//        package.setName((Name.getText().toString()));
//        package.setNuGuest((numberOfGuest.getText().toString()));
//        package.setFee(feePerDay.getText().toString());
//        package.setDescription((packageDescription.getText().toString()));
//        package.setAmenities((amenities));
//        package.setAmenities2((amenities2));


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child(uuid).setValue(pkg);
                        Toast.makeText(AddPackage.this, "Package Added..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddPackage.this, PackageMainView.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddPackage.this, "Error is Occured..", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}
