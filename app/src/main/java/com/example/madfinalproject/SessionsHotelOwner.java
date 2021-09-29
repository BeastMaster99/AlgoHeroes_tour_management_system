package com.example.madfinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionsHotelOwner {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CONTACTNUMBER = "contactnumber";

    SessionsHotelOwner (Context context){
        this.context = context;
        userSession = context.getSharedPreferences("hotelOwnerLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }


    //to set data
    public void createHotelOwnerLoginSession(String firstname, String lastname, String email, String contactnumber){
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FIRSTNAME,firstname);
        editor.putString(KEY_LASTNAME,lastname);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_CONTACTNUMBER,contactnumber);

        editor.commit();
    }

    //for get the data
    public HashMap<String,String> getHotelOwnerDetailsFromSessions(){
        HashMap <String, String> hotelOwnerData = new HashMap<>();

        hotelOwnerData.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME, null));
        hotelOwnerData.put(KEY_LASTNAME, userSession.getString(KEY_LASTNAME, null));
        hotelOwnerData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        hotelOwnerData.put(KEY_CONTACTNUMBER, userSession.getString(KEY_CONTACTNUMBER, null));

        return hotelOwnerData;
    }

    public boolean checkHotelOwnerLogin(){
        if (userSession.getBoolean(IS_LOGIN,false)){
            return true;
        }
        return false;
    }

    public void hotelOwnerLogout(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, SignIn.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
