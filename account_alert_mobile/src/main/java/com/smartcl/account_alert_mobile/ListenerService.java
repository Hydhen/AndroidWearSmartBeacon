package com.smartcl.account_alert_mobile;

import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;

import org.json.simple.JSONObject;

/**
 * Listener which reacts to the messages sent by the wearable device.
 * These messages can mean we detected a smartbeacon, or we got an answer from a question.
 * <p/>
 * When a smartbeacon is found, a question is sent to the wearable device.
 * The answer is saved within the database on the server side..
 */
public class ListenerService extends BaseListenerService {

    public static final String BEACON_ENTERED_PATH = "/beacon/entered/";
    public static final String ACCOUNTS_PATH = "/accounts/";
    public static final String GET_PREFERENCES_PATH = "/preferences/";
    public static final String SEND_PREFERENCES_PATH = "/preferences/data/";
    public static final String APP_OPEN_PATH = "/app/open/lcl/";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        showToast("message");
        final String path = messageEvent.getPath();
        switch (path) {
            case BEACON_ENTERED_PATH:
                beaconHasEntered(messageEvent);
                break;
            case GET_PREFERENCES_PATH:
                getPreferences(messageEvent);
                break;
            case APP_OPEN_PATH:
                openApp(messageEvent);
                break;
            default:
                showToast("Unknown message:" + path);
                break;
        }
    }

    private void beaconHasEntered(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        final long major = (long) json.get("major");
        final long minor = (long) json.get("minor");

        final String message = messageEvent.getPath() + "Major[" + major + "]Minor[" + minor + "]";
        showToast(message);

        JSONObject jsonToSend = new JSONObject();
        //TODO: make network request and send data.
        jsonToSend.put("accounts", "A");
        _messageSender.sendMessage(ACCOUNTS_PATH, jsonToSend);
    }

    private void getPreferences(MessageEvent messageEvent) {
        showToast("Get preferences");
        byte[] preferencesSerialized = getPreferencesSerialized(messageEvent);
        if (preferencesSerialized != null) {
            _messageSender.sendMessage(SEND_PREFERENCES_PATH, preferencesSerialized);
        }
    }

    private void openApp(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);

        final String appPackageName = (String) json.get("details");
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                                       Uri.parse("market://details?id=" + appPackageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id=" + appPackageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
