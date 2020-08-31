package com.crypto.quizapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;

public class CommonSharedPreference  {

    static SharedPreferences.Editor editor;

    public static void setsharedText(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }


    public static String getsharedText(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
        String value = preferences.getString(key, "");
        return value;
    }

    public static void deletesharedpref(Context context) {

        File sharedPreferenceFile = new File("/data/data/" + context.getPackageName() + "/shared_prefs/");
        File[] listFiles = sharedPreferenceFile.listFiles();
        for (File file : listFiles) {
            file.delete();
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit().clear();
        editor.commit();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString("firstTime", "yes");
        prefsEditor.commit();
    }

}
