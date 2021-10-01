package com.example.madfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class EditPackage extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    TextView actionBar;
    EditText Name, numberOfGuest, feePerDay, packageDescription, numRooms;
    Button packageSubmitBtn;
    Chip chipAirCondition, chipHouseKeeping, chipTel, chipSofa, chipDesk, chipSafe,
            chipMiniBar, chipRefrigerator, chipBathRooms, chipBottledWater, chipTV;
    Chip chipCityView, chipLandMark, chipFamilyRooms, chipNonSmokingRooms,
            chipSmokingRooms;
    ImageView imageBack;

    //IconSwitch switch1;

    String uuid;

    Package pkg = new Package();
    Package currentPackageData = new Package();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_package);

        //action bar
        actionBar = findViewById(R.id.actionBar);
        actionBar.setText("Edit Package");

        imageBack = findViewById(R.id.imageBack);

        //back button
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    EditPackage.super.onBackPressed();
                }
            }
        });

        //getting the current data
        currentPackageData = (Package) getIntent().getSerializableExtra("pkgObj");

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

        packageSubmitBtn    = findViewById(R.id.packageSubmitBtn);




        //setting data to the inputs
        //getting PackageID(uuid)
        uuid = currentPackageData.getUuid();

        //details
        Name.setText(currentPackageData.getName());
        numberOfGuest.setText(currentPackageData.getNuGuest());
        feePerDay.setText(currentPackageData.getFee());
        packageDescription.setText(currentPackageData.getDescription());
        numRooms.setText(currentPackageData.getnRooms());

        //room features chips
        if (currentPackageData.getRoomFeatures().contains("Air Condition")) {
            chipAirCondition.setChecked(true);
        }

        if (currentPackageData.getRoomFeatures().contains("House Keeping")) {
            chipHouseKeeping.setChecked(true);
        }

        if (currentPackageData.getRoomFeatures().contains("Telephone")) {
            chipTel.setChecked(true);
        }

        if (currentPackageData.getRoomFeatures().contains("Sofa")) {
            chipSofa.setChecked(true);
        }
        if (currentPackageData.getRoomFeatures().contains("Desk")) {
            chipDesk.setChecked(true);
        }
        if (currentPackageData.getRoomFeatures().contains("Safe")) {
            chipSafe.setChecked(true);
        }
        if (currentPackageData.getRoomFeatures().contains("Mini-Bar")) {
            chipMiniBar.setChecked(true);
        }
        if (currentPackageData.getRoomFeatures().contains("Refrigerator")) {
            chipRefrigerator.setChecked(true);
        }
        if (currentPackageData.getRoomFeatures().contains("Bath Rooms")) {
            chipBathRooms.setChecked(true);
        }
        if (currentPackageData.getRoomFeatures().contains("Bottled Water")) {
            chipBottledWater.setChecked(true);
        }
        if (currentPackageData.getRoomFeatures().contains("TV")) {
            chipTV.setChecked(true);
        }

        //room types chips

        if (currentPackageData.getRoomTypes().contains("City View")) {
            chipCityView.setChecked(true);
        }

        if (currentPackageData.getRoomTypes().contains("Land Mark")) {
            chipLandMark.setChecked(true);
        }

        if (currentPackageData.getRoomTypes().contains("Family Rooms")) {
            chipFamilyRooms.setChecked(true);
        }

        if (currentPackageData.getRoomTypes().contains("Non-Smoking Rooms")) {
            chipNonSmokingRooms.setChecked(true);
        }
        if (currentPackageData.getRoomTypes().contains("Smoking Rooms")) {
            chipSmokingRooms.setChecked(true);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();

        //creating the submit handler
        packageSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPackage();
            }


        });


    }

    private void uploadPackage() {

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Packages");

        ArrayList<String> roomFeatures = new ArrayList<>();
        //get data from chips
        if (chipAirCondition.isChecked()) {
            roomFeatures.add("Air Condition");
        }
        if (chipHouseKeeping.isChecked()) {
            roomFeatures.add("House Keeping");
        }
        if (chipTel.isChecked()) {
            roomFeatures.add("Telephone");
        }
        if (chipSofa.isChecked()) {
            roomFeatures.add("Sofa");
        }
        if (chipDesk.isChecked()) {
            roomFeatures.add("Desk");
        }
        if (chipSafe.isChecked()) {
            roomFeatures.add("Safe");
        }
        if (chipMiniBar.isChecked()) {
            roomFeatures.add("Mini-Bar");
        }
        if (chipRefrigerator.isChecked()) {
            roomFeatures.add("Refrigerator");
        }
        if (chipBathRooms.isChecked()) {
            roomFeatures.add("Bath Rooms");
        }
        if (chipBottledWater.isChecked()) {
            roomFeatures.add("Bottled Water");
        }
        if (chipTV.isChecked()) {
            roomFeatures.add("TV");
        }

        ArrayList<String> roomTypes = new ArrayList<>();

        if (chipCityView.isChecked()) {
            roomTypes.add("City View");
        }
        if (chipLandMark.isChecked()) {
            roomTypes.add("Land Mark");
        }
        if (chipFamilyRooms.isChecked()) {
            roomTypes.add("Family Rooms");
        }
        if (chipNonSmokingRooms.isChecked()) {
            roomTypes.add("Non-Smoking Rooms");
        }
        if (chipSmokingRooms.isChecked()) {
            roomTypes.add("Smoking Rooms");
        }

        String name         = Name.getText().toString();
        String guest        = numberOfGuest.getText().toString();
        String fee          = feePerDay.getText().toString();
        String description  = packageDescription.getText().toString();
        String nRooms       = numRooms.getText().toString();
        String hotelId      = getIntent().getStringExtra("hotelId");
        //passing data
        Package pkg         = new Package(name, guest, fee, description,
                roomFeatures,roomTypes,nRooms,uuid,hotelId);

        databaseReference.child(uuid).setValue(pkg);
        Toast.makeText(EditPackage.this, "Package Edited..", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(EditPackage.this, PackageMainView.class);
                intent.putExtra("uuid", uuid);
                startActivity(intent);
                finish();





    }

}