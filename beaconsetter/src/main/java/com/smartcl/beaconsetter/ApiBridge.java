package com.smartcl.beaconsetter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ApiBridge {
    static public String    getApiUrl(Activity activity){
        SharedPreferences settings = activity.getSharedPreferences("LclSmartbeaconPrefs", Context.MODE_MULTI_PROCESS);
        return settings.getString("api_url", null);
    }

    static public String    getSetBeaconUrl(Activity activity, String major, String minor, String profile, String usecase) {
        return getApiUrl(activity) + "beacon/set?major=" + major + "&minor=" + minor + "&profile=" + profile + "&usecase=" + usecase;
    }

    static public String    getGetBeaconUrl(Activity activity, String major, String minor) {
        return getApiUrl(activity) + "beacon/get?major=" + major + "&minor=" + minor;
    }

    static public String    getRemoveBeaconUrl(Activity activity, String major, String minor) {
        return getApiUrl(activity) + "beacon/delete?major=" + major + "&minor=" + minor;
    }

    public static String    getAllBeaconUrl(Activity activity) {
        return getApiUrl(activity) + "beacon/all";
    }

    public static String    getAllUsersUrl(Activity activity) {
        return getApiUrl(activity) + "user/all";
    }
}
