package com.smartcl.communicationlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * Created by bouca-_d on 22/02/15.
 */
public class LCLPreferences {
    public static final String NAME = "name";
    public static final String DEFAULTNAME = "User";
    public static final String ISPASS = "ACTOR_PASS";
    public static final String SERVER_URL = "api_url";

    static public void SetNameUser(Context context, String name, boolean is_passed) {
        SharedPreferences _pass = PreferenceManager.getDefaultSharedPreferences(context);
        _pass.edit().putBoolean(ISPASS, is_passed).apply();
    }

    static public String GetServerUrl(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SERVER_URL, "");
    }

    static public void Clear(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();
    }

    static public String GetNameUser(Context context) {
        SharedPreferences _getname = PreferenceManager.getDefaultSharedPreferences(context);
        return (_getname.getString(NAME, DEFAULTNAME));
    }

    static public Boolean GetStatusUser(Context context) {
        SharedPreferences _status = PreferenceManager.getDefaultSharedPreferences(context);
        return (_status.getBoolean(ISPASS, false));
    }

    static public Map<String, ?> GetDeserialized(byte[] b) {
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            Object o = si.readObject();

            Map<String, ?> map = (Map<String, ?>) o;
            return map;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    static public void WritePreferences(Context context, Map<String, ?> map) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String value = (String) entry.getValue();

            // Boolean
            if ("true".compareTo(value) == 0 || "false".compareTo(value) == 0) {
                boolean b = Boolean.parseBoolean(value);
                prefs.edit().putBoolean(entry.getKey(), b).apply();
                continue;
            }

            // Long
            try {
                long v = Long.parseLong(value);
                prefs.edit().putLong(entry.getKey(), v).apply();
                continue;
            } catch (NumberFormatException e) {

            }

            // String
            prefs.edit().putString(entry.getKey(), value).apply();
        }
    }

}
