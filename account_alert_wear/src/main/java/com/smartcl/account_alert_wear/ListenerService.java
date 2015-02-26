package com.smartcl.account_alert_wear;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * Listener which gets messages from the handheld device.
 */
public class ListenerService extends BaseListenerService {

    public static final String ACCOUNTS_PATH = "/accounts/";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        switch (path) {
            case ACCOUNTS_PATH:
                accountsGotten(messageEvent);
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

        final String mockMessage = "{ \"current_account\" : { \"money\" : 41000, \"state\" : \"red\" }, \"history\" : [" +
                "{ \"money\" : 90000, \"state\" : \"green\" }," +
                "{ \"money\" : 56000, \"state\" : \"orange\" }," +
                "{ \"money\" : 30000, \"state\" : \"red\" }] }";
        JSONObject jsonMocked = (JSONObject) JSONValue.parse(mockMessage);

        //TODO: if question, send if as parameter to the trigger notification (reuqire refactoring).
        NotificationsTrigger.TriggerNotification(this, jsonMocked);
    }
}
