package com.smartcl.communicationlibrary;

import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
}
