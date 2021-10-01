package com.example.madfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    //Creating object to access firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mad-project-754dc-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextView signUp = findViewById(R.id.createAccount);
        EditText userName = findViewById(R.id.userName);
        EditText password = findViewById(R.id.password);
        Button signInButton = findViewById(R.id.signInButton);


        //creating session hotel owner object and validating the login
        SessionsHotelOwner sessionsHotelOwner = new SessionsHotelOwner(SignIn.this);
        if (sessionsHotelOwner.checkHotelOwnerLogin() == true){
            Intent intent = new Intent(this, HotelOwnerMainView.class);
            startActivity(intent);
            finish();
        }

        //creating session tour guide object and validating the login
        SessionsTourGuide sessionsTourGuide = new SessionsTourGuide(SignIn.this);
        if (sessionsTourGuide.checkTourGuideLogin() == true) {
            Intent intent = new Intent(this, TourGuideMainView.class);
            startActivity(intent);
            finish();
        }

        //creating session traveler object and validating the login
        SessionsTraveler sessionsTraveler = new SessionsTraveler(SignIn.this);
        if (sessionsTraveler.checkTravelerGuideLogin() == true){
            Intent intent = new Intent(this, TravelerMainView.class);
            startActivity(intent);
            finish();
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override

            //This onClick listener called when user taps Sign Up
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Creating the dialog object

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //This onClick listener called when user taps Sign In
            public void onClick(View view) {

                //Turning username and password to Strings
                String userNameText = userName.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                String email = decodeUserEmail(userNameText);//Encoding the email

                if (email.isEmpty() || passwordText.isEmpty()) {  //Send a Toast if the fields are empty
                    //Toast toast = Toast.makeText(SignIn.this,"Please Enter Your Credentials", Toast.LENGTH_LONG);
                    //toast.show();

                    //Telling user that they must enter there credentials to sign in to the system
                    builder.setTitle("Alert!!").setMessage("Please Enter Your Credentials")
                            .setCancelable(true)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();


                } else {
                    //Starting of the Hotel Owner Sign in validations
                    databaseReference.child("Hotel Owner").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(email)) { //Validating the email

                                String getPassword = snapshot.child(email).child("password").getValue(String.class);

                                if (getPassword.equals(passwordText)) { //Validating the password

                                    //getting hotel owner details from the firebase
                                    String firstNameHO = snapshot.child(email).child("firstname").getValue(String.class);
                                    String lastNameHO = snapshot.child(email).child("lastname").getValue(String.class);
                                    String emailHO = snapshot.child(email).child("email").getValue(String.class);
                                    String contactNumberHO = snapshot.child(email).child("contactnumber").getValue(String.class);

                                    //Creating the session
                                    sessionsHotelOwner.createHotelOwnerLoginSession(firstNameHO,lastNameHO,emailHO,contactNumberHO);

                                    Toast toast = Toast.makeText(SignIn.this, "Successfully Sign In as a Hotel Owner", Toast.LENGTH_LONG);
                                    toast.show();

                                    //Redirect User to the HotelOwnerMainView page
                                    Intent intent2 = new Intent(SignIn.this, HotelOwnerMainView.class);
                                    startActivity(intent2);
                                    finish();

                                } else {
                                    //Toast toast = Toast.makeText(SignIn.this,"Password you entered must be wrong", Toast.LENGTH_LONG);
                                    //toast.show();

                                    //Telling user that the password entered is wrong
                                    builder.setTitle("Alert!!").setMessage("Password you entered is invalid")
                                            .setCancelable(true)
                                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            })
                                            .show();
                                }
                            } else {
                                //Starting of the Tour Guide Sign in validations
                                databaseReference.child("Tour Guide").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.hasChild(email)) { //Validating the email

                                            String getPassword = snapshot.child(email).child("password").getValue(String.class);

                                            if (getPassword.equals(passwordText)) { //Validating the password

                                                //getting hotel owner details from the firebase
                                                String firstNameTG = snapshot.child(email).child("firstname").getValue(String.class);
                                                String lastNameTG = snapshot.child(email).child("lastname").getValue(String.class);
                                                String emailTG = snapshot.child(email).child("email").getValue(String.class);
                                                String contactNumberTG = snapshot.child(email).child("contactnumber").getValue(String.class);

                                                System.out.println(firstNameTG);
                                                //Creating the session
                                                sessionsTourGuide.createTourGuideLoginSession(firstNameTG, lastNameTG, emailTG, contactNumberTG);

                                                Toast toast = Toast.makeText(SignIn.this, "Successfully Sign In as a Tour Guide", Toast.LENGTH_LONG);
                                                toast.show();


                                                //Redirect User to the TourGuideMainView page
                                                Intent intent2 = new Intent(SignIn.this, TourGuideMainView.class);
                                                startActivity(intent2);
                                                finish();

                                            } else {
                                                //Toast toast = Toast.makeText(SignIn.this,"Password you entered must be wrong", Toast.LENGTH_LONG);
                                                //toast.show();

                                                //Telling user that the password entered is wrong
                                                builder.setTitle("Alert!!").setMessage("Password you entered is invalid")
                                                        .setCancelable(true)
                                                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.cancel();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        } else {
                                            //Starting of the Traveler Sign in validations
                                            databaseReference.child("Traveler").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    if (snapshot.hasChild(email)) { //Validating the email

                                                        String getPassword = snapshot.child(email).child("password").getValue(String.class);

                                                        if (getPassword.equals(passwordText)) { //Validating the password

                                                            //getting Traveler details from the firebase
                                                            String firstNamTR = snapshot.child(email).child("firstname").getValue(String.class);
                                                            String lastNameTR = snapshot.child(email).child("lastname").getValue(String.class);
                                                            String emailTR = snapshot.child(email).child("email").getValue(String.class);
                                                            String contactNumberTR = snapshot.child(email).child("contactnumber").getValue(String.class);


                                                            //Creating the session
                                                            sessionsTraveler.createTravelerLoginSession(firstNamTR,lastNameTR,emailTR,contactNumberTR);

                                                            Toast toast = Toast.makeText(SignIn.this, "Successfully Sign In as a Traveler", Toast.LENGTH_LONG);
                                                            toast.show();

                                                            //Redirect User to the HotelOwnerMainView page
                                                            Intent intent2 = new Intent(SignIn.this, TravelerMainView.class);
                                                            startActivity(intent2);
                                                            finish();

                                                        } else {
                                                            //Toast toast = Toast.makeText(SignIn.this,"Password you entered must be wrong", Toast.LENGTH_LONG);
                                                            //toast.show();

                                                            //Telling user that the password entered is wrong
                                                            builder.setTitle("Alert!!").setMessage("Password you entered is invalid")
                                                                    .setCancelable(true)
                                                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            dialogInterface.cancel();
                                                                        }
                                                                    })
                                                                    .show();
                                                        }
                                                    } else {
                                                        //Toast toast = Toast.makeText(SignIn.this,"Invalid User Name", Toast.LENGTH_LONG);
                                                        //toast.show();

                                                        //Telling user that Email or password is incorrect
                                                        builder.setTitle("Alert!!").setMessage("Invalid User Name or Password")
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

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {//If te database connection lost sending a Toast massage to th user
                                                    Toast.makeText(SignIn.this, "Lost the connection with the Database", Toast.LENGTH_SHORT).show();

                                                }
                                            }); //End of the Traveler Validation Part
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {//If te database connection lost sending a Toast massage to th user
                                        Toast.makeText(SignIn.this, "Lost the connection with the Database", Toast.LENGTH_SHORT).show();

                                    }
                                }); //End of the Tour Guide Validation Part
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {//If te database connection lost sending a Toast massage to th user
                            Toast.makeText(SignIn.this, "Lost the connection with the Database", Toast.LENGTH_SHORT).show();

                        }
                    });
                    //End of the Hotel Owner Validation Part


                }

            }
        });

    }


    //to change the operators
    static String decodeUserEmail(String email) {

        return email.replace(".", ",");
    }
}


