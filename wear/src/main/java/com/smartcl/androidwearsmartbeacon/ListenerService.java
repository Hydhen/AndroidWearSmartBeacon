package com.smartcl.androidwearsmartbeacon;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;

import org.json.simple.JSONObject;


/**
 * Listener which gets messages from the handheld device.
 */
public class ListenerService extends BaseListenerService {

    public static final String QUESTION_QUESTION_PATH = "/question/question";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        switch (path) {
            case QUESTION_QUESTION_PATH:
                questionGotten(messageEvent);
                break;
            default:
                showToast("Unknown message:" + path);
                break;
        }
    }

    private void questionGotten(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        final String question = (String) json.get("question");

        // TODO: check format
        showToast(messageEvent.getPath());

        //TODO: if question, send if as parameter to the trigger notification (reuqire refactoring).
        NotificationsTrigger.TriggerNotification(this, question);
    }

}
