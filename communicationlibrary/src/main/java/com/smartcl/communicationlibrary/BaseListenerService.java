package com.smartcl.communicationlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Should be implemented by listeners which exchange message with MessageSender.
 */
public class BaseListenerService extends WearableListenerService {
    protected MessageSender _messageSender = null;

    @Override
    public void onCreate() {
        super.onCreate();
        _messageSender = new MessageSender(this);
    }

    @Override
    public void onDestroy() {
        _messageSender.waitForAllThreadsToFinish();
        super.onDestroy();
    }

    protected void showToast(final String message) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected JSONObject extractJsonFromMessage(MessageEvent messageEvent) {
        final byte[] data = messageEvent.getData();
        final String strData = new String(data);

        JSONObject json = (JSONObject) JSONValue.parse(strData);
        return json;
    }

    protected SharedPreferences getCommonSharedPreferences() {
        Context context = null;
        try {
            context = createPackageContext("com.smartcl.beaconsetter",
                                           Context.MODE_WORLD_WRITEABLE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (context != null) {
            SharedPreferences prefs = context
                    .getSharedPreferences("LclSmartbeaconPrefs", MODE_WORLD_READABLE);
            return prefs;
        }
        return null;
    }

    protected byte[] getPreferencesSerialized(MessageEvent messageEvent) {
        SharedPreferences prefs = getCommonSharedPreferences();
        Map<String, ?> all = prefs.getAll();
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(all);
            so.flush();
            return bo.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
