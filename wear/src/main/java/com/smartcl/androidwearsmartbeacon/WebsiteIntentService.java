package com.smartcl.androidwearsmartbeacon;

import android.app.IntentService;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;

import com.smartcl.communicationlibrary.MessageSender;

import org.json.simple.JSONObject;

/**
 * Open a website from the wearable to the handheld.
 */
public class WebsiteIntentService extends IntentService {

    private static final String WEBSITE_OPEN_PATH = "/website/open/lcl/";
    private MessageSender _messageSender = null;

    public WebsiteIntentService() {
        super("WebsiteIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        _messageSender = new MessageSender(getApplicationContext());

        Intent openOnPhoneIntent = new Intent(this, ConfirmationActivity.class);
        openOnPhoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openOnPhoneIntent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                                   ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
        startActivity(openOnPhoneIntent);

        _messageSender = new MessageSender(getApplicationContext());
        JSONObject json = new JSONObject();
        json.put("path", "https://particuliers.secure.lcl.fr/index.html");
        _messageSender.sendMessage(WEBSITE_OPEN_PATH, json);
    }

}
