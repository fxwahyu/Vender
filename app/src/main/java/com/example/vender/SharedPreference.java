package com.example.vender;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.vender.Model.User;
import com.google.gson.Gson;

public class SharedPreference {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public static final String IS_LOGGED_IN="is_logged_in"; //name variabel
    public static final String USER="user";
    private Gson gson = new Gson();

    public SharedPreference(Context context){
        this.preferences= PreferenceManager.getDefaultSharedPreferences(context);
        this.editor=this.preferences.edit();
    }

    public void clearPreference(String tag){
        editor.remove(tag);
        editor.apply();
    }

    public void setUser(User user){
        String model=gson.toJson(user);
        editor.putString(USER,model);
        editor.commit();
    }

    public User getUser(){
        String json=preferences.getString(USER,null);
        return gson.fromJson(json,User.class);
    }

    public void setIsLoggedIn(boolean isLoggedIn){
        editor.putBoolean(IS_LOGGED_IN,isLoggedIn); //mengubah nilai jika sudah log in
        editor.commit(); //menyimpan
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGGED_IN, false); //mengambil nilai berdasarkan variable is logged in
    }
}
