package com.smartcl.androidwearsmartbeacon;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;
import com.smartcl.communicationlibrary.LCLPreferences;

import org.json.simple.JSONObject;

import java.util.Map;


/**
 * Listener which gets messages from the handheld device.
 */
public class ListenerService extends BaseListenerService {

    public static final String QUESTION_QUESTION_PATH = "/question/question";
    public static final String SEND_PREFERENCES_PATH = "/preferences/data/";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        switch (path) {
            case QUESTION_QUESTION_PATH:
                questionGotten(messageEvent);
                break;
            case SEND_PREFERENCES_PATH:
                preferencesGotten(messageEvent);
                break;
            default:
                showToast("Unknown message:" + path);
                break;
        }
    }

    private void preferencesGotten(MessageEvent messageEvent) {
        byte[] data = messageEvent.getData();
        Map<String, ?> map = LCLPreferences.GetDeserialized(data);
        LCLPreferences.WritePreferences(this, map);
    }

    private void questionGotten(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        final String question = (String) json.get("question");
        NotificationsTrigger.TriggerNotification(this, question);
    }

}
