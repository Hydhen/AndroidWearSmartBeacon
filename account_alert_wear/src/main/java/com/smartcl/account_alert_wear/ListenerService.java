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
       // JSONObject json = extractJsonFromMessage(messageEvent);

        showToast("accounts gotten");

        // TODO: get this message from json object gotten.
        final String mockMessage =
                "{ \"accounts\" : [" +
                        "{ \"name\" : \"me\", \"account\" : [" +
                        "{ \"money\" : 90000, \"state\" : \"green\", \"date\" : \"01/03/2015\" }," +
                        "{ \"money\" : 50000, \"state\" : \"yellow\", \"date\" : \"15/03/2015\" }," +
                        "{ \"money\" : 30000, \"state\" : \"red\", \"date\" : \"01/04/2015\" }" +
                        "]}," +
                        "{ \"name\" : \"son\", \"account\" : [" +
                        "{ \"money\" : 9000, \"state\" : \"red\", \"date\" : \"01/03/2015\" }," +
                        "{ \"money\" : 5000, \"state\" : \"green\", \"date\" : \"15/03/2015\" }," +
                        "{ \"money\" : 3000, \"state\" : \"red\", \"date\" : \"01/04/2015\" }" +
                        "]}" +
                        "]}";
        JSONObject jsonMocked = (JSONObject) JSONValue.parse(mockMessage);

        NotificationsTrigger.TriggerNotification(this, jsonMocked);
    }

    private void preferencesGotten(MessageEvent messageEvent) {
        showToast("prefs gotten");
        byte[] data = messageEvent.getData();
        Map<String, ?> map = LCLPreferences.GetDeserialized(data);
        LCLPreferences.WritePreferences(this, map);

        // TODO: this is a biiiig cheat.
        // The problem is when I get the preferences, the message for accounts is lost !
        // Since the server connection will not be done, we can cheat like this...
        // FIXME: Find a fix for message loses.
        accountsGotten(messageEvent);
    }
}
