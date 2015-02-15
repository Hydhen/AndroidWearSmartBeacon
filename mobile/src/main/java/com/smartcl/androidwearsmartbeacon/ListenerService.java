package com.smartcl.androidwearsmartbeacon;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * Listener which reacts to the messages sent by the wearable device.
 * These messages can mean we detected a smartbeacon, or we got an answer from a question.
 *
 * When a smartbeacon is found, a question is sent to the wearable device.
 * The answer is saved within the database on the server side..
 */
public class ListenerService extends BaseListenerService {

    public static final String BEACON_ENTERED_PATH = "/beacon/entered/";
    public static final String QUESTION_ANSWER_PATH = "/question/answer/";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        switch (path) {
            case BEACON_ENTERED_PATH:
                beaconHasEntered(messageEvent);
                break;
            case QUESTION_ANSWER_PATH:
                getAnswer(messageEvent);
                break;
            default:
                break;
        }
    }

    private void getAnswer(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        final String answer = (String) json.get("answer");

        final String message = messageEvent.getPath() + "Answer[" + answer + "]";
        showToast(message);

        // TODO: Perform actions like network requests, etc...
    }

    private void beaconHasEntered(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        long major = (long) json.get("major");
        long minor = (long) json.get("minor");

        final String message = messageEvent.getPath() + "Major[" + major + "]Minor[" + minor + "]";
        showToast(message);

        // TODO: Perform actions like network requests, etc...
        // TODO: send question

        _messageSender.sendMessage(message);
    }

    private JSONObject extractJsonFromMessage(MessageEvent messageEvent) {
        final byte[] data = messageEvent.getData();
        final String strData = new String(data);

        JSONObject json = (JSONObject) JSONValue.parse(strData);
        return json;
    }
}