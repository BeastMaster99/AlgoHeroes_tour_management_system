package com.example.madfinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionsTraveler {
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CONTACTNUMBER = "contactnumber";

    SessionsTraveler (Context context){
        this.context = context;
        userSession = context.getSharedPreferences("TravelerLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }


    //to set data
    public void createTravelerLoginSession(String firstname, String lastname, String email, String contactnumber){
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FIRSTNAME,firstname);
        editor.putString(KEY_LASTNAME,lastname);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_CONTACTNUMBER,contactnumber);

        editor.commit();
    }

    //for get the data
    public HashMap<String,String> getTravelerDetailsFromSessions(){
        HashMap <String, String> travelerData = new HashMap<>();

        travelerData.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME, null));
        travelerData.put(KEY_LASTNAME, userSession.getString(KEY_LASTNAME, null));
        travelerData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        travelerData.put(KEY_CONTACTNUMBER, userSession.getString(KEY_CONTACTNUMBER, null));

        return travelerData;
    }

    public boolean checkTravelerGuideLogin(){
        if (userSession.getBoolean(IS_LOGIN,false)){
//            Intent intent = new Intent(context, TravelerMainView.class);
//            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public void travelerLogout(){
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, SignIn.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
