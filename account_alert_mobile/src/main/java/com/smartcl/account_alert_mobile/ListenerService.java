package com.smartcl.account_alert_mobile;

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

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        showToast("message");
        final String path = messageEvent.getPath();
        switch (path) {
            case BEACON_ENTERED_PATH:
                beaconHasEntered(messageEvent);
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
        //TODO: send message
        jsonToSend.put("accounts", "A");
        _messageSender.sendMessage(ACCOUNTS_PATH, jsonToSend);
    }

}
