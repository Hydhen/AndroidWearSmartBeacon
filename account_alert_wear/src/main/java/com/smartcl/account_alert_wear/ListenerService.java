package com.smartcl.account_alert_wear;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;
import com.smartcl.communicationlibrary.LCLPreferences;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Map;


/**
 * Listener which gets messages from the handheld device.
 */
public class ListenerService extends BaseListenerService {

    public static final String ACCOUNTS_PATH = "/accounts/";
    public static final String SEND_PREFERENCES_PATH = "/preferences/data/";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        switch (path) {
            case ACCOUNTS_PATH:
                accountsGotten(messageEvent);
                break;
            case SEND_PREFERENCES_PATH:
                preferencesGotten(messageEvent);
                break;
            default:
                showToast("Unknown message:" + path);
                break;
        }
    }

    private void accountsGotten(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);

        // TODO: check format
        showToast(messageEvent.getPath());

        final String mockMessage =
                "{ \"current_account\" : { \"money\" : 41000, \"state\" : \"red\", \"date\" : \"26/02/2015\" }, \"previsions\" : [" +
                        "{ \"money\" : 90000, \"state\" : \"green\", \"date\" : \"01/03/2015\" }," +
                        "{ \"money\" : 56000, \"state\" : \"orange\", \"date\" : \"15/03/2015\" }," +
                        "{ \"money\" : 30000, \"state\" : \"red\", \"date\" : \"01/04/2015\" }] }";
        JSONObject jsonMocked = (JSONObject) JSONValue.parse(mockMessage);

        NotificationsTrigger.TriggerNotification(this, jsonMocked);
    }

    private void preferencesGotten(MessageEvent messageEvent) {
        showToast("prefs gotten");
        byte[] data = messageEvent.getData();
        Map<String, ?> map = LCLPreferences.GetDeserialized(data);
        LCLPreferences.WritePreferences(this, map);
    }
}
