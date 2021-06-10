package com.example.shopbanchau.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedReferences {
    private static final String SHARED_REFERENCES = "SHARED_REFERENCES_APP";
    private Context mContext;

    public MySharedReferences(Context mContext) {
        this.mContext = mContext;
    }

    public void putValue(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_REFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }
    public String getValueString(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_REFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }


    public void putValue(String key, int value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_REFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public int getValueInt(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_REFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }


    public void remove(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_REFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
