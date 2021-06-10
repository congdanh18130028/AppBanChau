package com.example.shopbanchau.utils;

import android.content.Context;

public class DataLocalManager {
    private static final String TOKEN = "TOKEN";
    private static final String ID = "ID";
    private static final String CARTID = "CARTID";
    private static DataLocalManager instance;
    private MySharedReferences mySharedReferences;
    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedReferences = new MySharedReferences(context);

    }
    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }
    public static void setToken(String token){
        DataLocalManager.getInstance().mySharedReferences.putValue(TOKEN, token);

    }

    public static String getToken(){
        return DataLocalManager.getInstance().mySharedReferences.getValueString(TOKEN);
    }

    public static void removeToken(){
        DataLocalManager.getInstance().mySharedReferences.remove(TOKEN);
    }

    public static void setId(int id){
        DataLocalManager.getInstance().mySharedReferences.putValue(ID, id);
    }

    public static int getId(){
        return DataLocalManager.getInstance().mySharedReferences.getValueInt(ID);
    }

    public static void removeID(){
        DataLocalManager.getInstance().mySharedReferences.remove(ID);
    }


    public static void setCartId(int id) {
        DataLocalManager.getInstance().mySharedReferences.putValue(CARTID, id);
    }

    public static int getCartId(){
        return DataLocalManager.getInstance().mySharedReferences.getValueInt(CARTID);
    }

    public static void removeCartID(){
        DataLocalManager.getInstance().mySharedReferences.remove(CARTID);
    }


}
