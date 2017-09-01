package com.example.szymon.events_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.szymon.events_mobile.Activity.LoginActivity;

import java.util.HashMap;

/**
 * Created by Szymon on 01.05.2017.
 */

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context_;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SESSION_USER";
    private static final String IS_LOGIN = "is_login";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String KEY_ACCESS_TOKEN = "access_token";

    public SessionManagement(Context context) {
        this.context_ = context;
        sharedPreferences = context_.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String user_id, String user_name, String access_token) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_ID, user_id);
        editor.putString(USER_NAME, user_name);
        editor.putString(KEY_ACCESS_TOKEN, access_token);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent intent = new Intent(context_, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context_.startActivity(intent);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(USER_ID, sharedPreferences.getString(USER_ID, null));
        user.put(USER_NAME, sharedPreferences.getString(USER_NAME, null));
        user.put(KEY_ACCESS_TOKEN, sharedPreferences.getString(KEY_ACCESS_TOKEN, null));
        return user;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context_, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context_.startActivity(intent);
    }
}
