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

        // TODO: get this message from json object gotten.
        final String mockMessage =
                "{ \"accounts\" : [" +
                        "{ \"name\" : \"moi\", \"account\" : [" +
                        "{ \"money\" : 10000.31, \"state\" : \"green\", \"date\" : \"01/03/2015\" }," +
                        "{ \"money\" : 5300.84, \"state\" : \"yellow\", \"date\" : \"15/03/2015\" }," +
                        "{ \"money\" : -250.56, \"state\" : \"red\", \"date\" : \"01/04/2015\" }" +
                        "]}," +
                        "{ \"name\" : \"mon fils\", \"account\" : [" +
                        "{ \"money\" : -1025.95, \"state\" : \"red\", \"date\" : \"01/03/2015\" }," +
                        "{ \"money\" : 2098.74, \"state\" : \"green\", \"date\" : \"15/03/2015\" }," +
                        "{ \"money\" : -3.56, \"state\" : \"red\", \"date\" : \"01/04/2015\" }" +
                        "]}" +
                        "]}";
        JSONObject jsonMocked = (JSONObject) JSONValue.parse(mockMessage);

        NotificationsTrigger.TriggerNotification(this, jsonMocked);
    }

    private void preferencesGotten(MessageEvent messageEvent) {
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
