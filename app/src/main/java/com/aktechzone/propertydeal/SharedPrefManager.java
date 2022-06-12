package com.aktechzone.propertydeal;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static String SHARED_PREF_LOGIN = "login";
    public static String SHARED_PREF_REGISTER = "register";
    public static final String SHARED_PREF_FAV = "favProp";
    public static final String SHARED_PREF_PROPERTY = "property";

    public static final String KEY_Password = "password";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ID = "id";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_API_TOKEN = "api_token";

    public static final String KEY_PROP_LOCATION = "propLocation";
    public static final String KEY_LOCATION = "location";


    public static SharedPrefManager mInstance;
    public static Context mCtx;
    public SharedPreferences sharedPrefs;
    public SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }





    public boolean isLoggedIn() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_EMAIL, null) != null;
    }

    public void logout() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }

    public String getUserType(){
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_TYPE, "");
    }

    public String getPassword(){
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_Password, "");
    }

    public void userProfile(String userName, String userEmail, String phone, String apiToken,String type,String id) {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(KEY_NAME, userName);
        editor.putString(KEY_EMAIL, userEmail);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_API_TOKEN, apiToken);
        editor.putString(KEY_TYPE, type);
        editor.putString(KEY_ID,id);
        editor.apply();
    }

    public String getId() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_ID, "");
    }

    public String getName() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_NAME, "");
    }

    public String getEmail() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_EMAIL, "");
    }

    public String getPhone() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_PHONE, "");
    }

    /////////////////////////////////////////////////


    public void userLogin(String Email_ID, String Password,String type) {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_LOGIN, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.putString(KEY_EMAIL, Email_ID);
        editor.putString(KEY_Password, Password);
        editor.putString(KEY_TYPE, type);
        editor.apply();
    }

    public void clearRegPref() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }


    public boolean isUserExist(String email, String pass) {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
        if (sharedPrefs.getString(KEY_EMAIL, "").equals(email) && sharedPrefs.getString(KEY_Password, "").equals(pass)) {
            return true;
        }
        return false;
    }

    public void saveLocation(String value) {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_PROPERTY, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.putString(KEY_LOCATION, value);
        editor.apply();
        editor.commit();
    }

    public boolean isLocationSelected() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_PROPERTY, Context.MODE_PRIVATE);
        if (sharedPrefs.getString(KEY_LOCATION, "").equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public String getPropLocation() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_PROPERTY, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_LOCATION, "");
    }

    public void clearLocation() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_PROPERTY, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.remove(KEY_LOCATION);
        editor.apply();
        editor.commit();
    }

    public void clearLocationPref() {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_PROPERTY, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();
    }

    public boolean isPropExist(String key) {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_FAV, Context.MODE_PRIVATE);
        return sharedPrefs.contains(key);
    }

    public int getFavourite(String key) {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_FAV, Context.MODE_PRIVATE);
        return sharedPrefs.getInt(key, -1);
    }

    public void saveFavourite(String key, int value) {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_FAV, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void removeFavourite(String key) {
        sharedPrefs = mCtx.getSharedPreferences(SHARED_PREF_FAV, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        editor.remove(key);
        editor.apply();
    }
}
