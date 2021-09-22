package com.example.madfinalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionsTourGuide {
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CONTACTNUMBER = "contactnumber";

    SessionsTourGuide (Context context){
        this.context = context;
        userSession = context.getSharedPreferences("TourGuideLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }


    //to set data
    public void createTourGuideLoginSession(String firstname, String lastname, String email, String contactnumber){
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FIRSTNAME,firstname);
        editor.putString(KEY_LASTNAME,lastname);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_CONTACTNUMBER,contactnumber);

        editor.commit();
    }

    //for get the data
    public HashMap<String,String> getTourGuideDetailsFromSessions(){
        HashMap <String, String> tourGuideData = new HashMap<>();

        tourGuideData.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME, null));
        tourGuideData.put(KEY_LASTNAME, userSession.getString(KEY_LASTNAME, null));
        tourGuideData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        tourGuideData.put(KEY_CONTACTNUMBER, userSession.getString(KEY_CONTACTNUMBER, null));

        return tourGuideData;
    }

    public void checkTourGuideLogin(){
        if (userSession.getBoolean(IS_LOGIN,false)){
            Intent intent = new Intent(context, TourGuideMainView.class);
            context.startActivity(intent);

        }
    }

    public void tourGuideLogout(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, SignIn.class);
        context.startActivity(intent);
    }
}
