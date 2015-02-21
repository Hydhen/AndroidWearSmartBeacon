package com.smartcl.androidwearsmartbeacon;

import android.content.Intent;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * Listener which reacts to the messages sent by the wearable device.
 * These messages can mean we detected a smartbeacon, or we got an answer from a question.
 * <p/>
 * When a smartbeacon is found, a question is sent to the wearable device.
 * The answer is saved within the database on the server side..
 */
public class ListenerService extends BaseListenerService {

    public static final String BEACON_ENTERED_PATH = "/beacon/entered/";
    public static final String QUESTION_ANSWER_PATH = "/question/answer/";
    public static final String WEBSITE_OPEN_PATH = "/website/open/lcl/";

    public static final String GET_USER_INFO_URL
            = "http://ec2-54-93-111-136.eu-central-1.compute.amazonaws.com:21996/user/signin";

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
            case WEBSITE_OPEN_PATH:
                openWebsite(messageEvent);
                break;
            default:
                showToast("Unknown message:" + path);
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
        final long major = (long) json.get("major");
        final long minor = (long) json.get("minor");

        final String message = messageEvent.getPath() + "Major[" + major + "]Minor[" + minor + "]";
        showToast(message);

        // TODO: Perform actions like network requests, etc...
        // TODO: send question
        //TEST

        NetworkOperation network = new NetworkOperation(this);
        network.operationGet(NetworkOperation.getAskQuestionUrl(),
                             new Response.Listener() {
                                 @Override
                                 public void onResponse(Object o) {
                                     showToast("ON RESONSE");
                                 }
                             },
                             new Response.ErrorListener() {
                                 @Override
                                 public void onErrorResponse(VolleyError error) {
                                     showToast("ON ERROR RESONSE");
                                 }
                             });
        _messageSender.sendMessage(message);
    }

    private void openWebsite(MessageEvent messageEvent) {
        JSONObject json = extractJsonFromMessage(messageEvent);
        final String path = (String) json.get("path");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(browserIntent);
    }

    private JSONObject extractJsonFromMessage(MessageEvent messageEvent) {
        final byte[] data = messageEvent.getData();
        final String strData = new String(data);

        JSONObject json = (JSONObject) JSONValue.parse(strData);
        return json;
    }
}
