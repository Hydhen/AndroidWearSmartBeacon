package com.smartcl.communicationlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bouca-_d on 22/02/15.
 */
public class LCLPreferences {
    public static final String NAME = "ACTOR_NAME";
    public static final String DEFAULTNAME = "User";
    public static final String ISPASS = "ACTOR_PASS";

    static public void SetNameUser(Context context, String name, boolean is_passed) {
        SetNameUser(context, name);
        SharedPreferences _pass = PreferenceManager.getDefaultSharedPreferences(context);
        _pass.edit().putBoolean(ISPASS, is_passed).commit();
    }

    static public void SetNameUser(Context context, String name) {
        SharedPreferences _name = PreferenceManager.getDefaultSharedPreferences(context);
        _name.edit().putString(NAME, name).commit();
    }

    static public String GetNameUser(Context context) {
        SharedPreferences _getname = PreferenceManager.getDefaultSharedPreferences(context);
        return (_getname.getString(NAME, DEFAULTNAME));
    }

    static public Boolean GetStatusUser(Context context) {
        SharedPreferences _status = PreferenceManager.getDefaultSharedPreferences(context);
        return (_status.getBoolean(ISPASS, false));
    }}
